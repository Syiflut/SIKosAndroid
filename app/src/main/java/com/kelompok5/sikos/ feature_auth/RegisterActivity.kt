package com.kelompok5.sikos.feature_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.etNamaRegister)
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val rgRole = findViewById<RadioGroup>(R.id.rgRoleRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvKembaliLogin = findViewById<TextView>(R.id.tvKembaliLogin)

        btnRegister.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Mengambil pilihan Role dari RadioButton
            val roleTerpilihId = rgRole.checkedRadioButtonId
            val roleInput = if (roleTerpilihId == R.id.rbPenghuni) "PENGHUNI" else "PEMILIK"

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val mAuth = com.google.firebase.auth.FirebaseAuth.getInstance()

                // 1. Daftarkan Email & Password ke Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = mAuth.currentUser?.uid ?: ""

                            // 2. Siapkan data yang akan disimpan ke Realtime Database
                            val userMap = HashMap<String, Any>()
                            userMap["uid"] = uid
                            userMap["nama"] = nama
                            userMap["email"] = email
                            userMap["role"] = roleInput

                            // 3. Simpan data ke node "pengguna" -> "UID_USER" di Realtime Database (Disesuaikan dengan Firebase Baru)
                            val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance()
                                .getReference("pengguna").child(uid)

                            dbRef.setValue(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                                    // Lempar kembali ke halaman Login setelah sukses
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal menyimpan ke database: ${e.message}", Toast.LENGTH_LONG).show()
                                }

                        } else {
                            Toast.makeText(this, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        // Jika user salah pencet dan mau balik ke halaman login
        tvKembaliLogin.setOnClickListener {
            finish()
        }
    }
}