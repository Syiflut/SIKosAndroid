package com.kelompok5.sikos.feature_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kelompok5.sikos.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmailDaftar: EditText
    private lateinit var etPasswordDaftar: EditText
    private lateinit var btnDaftar: Button
    private lateinit var tvKembaliLogin: TextView // Sudah disamakan dengan XML

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // SINKRONISASI ID: Sudah pas 100% dengan activity_register.xml kamu
        etEmailDaftar = findViewById(R.id.etEmailRegister)
        etPasswordDaftar = findViewById(R.id.etPasswordRegister)
        btnDaftar = findViewById(R.id.btnRegister)
        tvKembaliLogin = findViewById(R.id.tvKembaliLogin)

<<<<<<< HEAD
        btnDaftar.setOnClickListener {
            prosesDaftarFirebase()
=======
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
>>>>>>> 6c20ff667c1c6da863ea6ab00e84687a9fa910c9
        }

        tvKembaliLogin.setOnClickListener {
            finish() // Menutup halaman register dan otomatis balik ke LoginActivity
        }
    }

    private fun prosesDaftarFirebase() {
        val email = etEmailDaftar.text.toString().trim()
        val password = etPasswordDaftar.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter ya!", Toast.LENGTH_SHORT).show()
            return
        }

        btnDaftar.text = "Mendaftarkan..."
        btnDaftar.isEnabled = false

        // Mengirim data pendaftaran ke Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnDaftar.text = "Daftar Akun"
                btnDaftar.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(this, "Akun berhasil dibuat! Silakan login.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal daftar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}