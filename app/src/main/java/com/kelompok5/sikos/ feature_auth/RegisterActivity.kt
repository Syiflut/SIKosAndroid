package com.kelompok5.sikos.feature_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelompok5.sikos.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNamaDaftar: EditText
    private lateinit var etEmailDaftar: EditText
    private lateinit var etPasswordDaftar: EditText
    private lateinit var rgRole: RadioGroup
    private lateinit var btnDaftar: Button
    private lateinit var tvKembaliLogin: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // SINKRONISASI ID: Disamakan dengan activity_register.xml kamu
        etNamaDaftar = findViewById(R.id.etNamaRegister)
        etEmailDaftar = findViewById(R.id.etEmailRegister)
        etPasswordDaftar = findViewById(R.id.etPasswordRegister)
        rgRole = findViewById(R.id.rgRoleRegister)
        btnDaftar = findViewById(R.id.btnRegister)
        tvKembaliLogin = findViewById(R.id.tvKembaliLogin)

        btnDaftar.setOnClickListener {
            prosesDaftarFirebase()
        }

        tvKembaliLogin.setOnClickListener {
            finish() // Menutup halaman register dan balik ke LoginActivity
        }
    }

    private fun prosesDaftarFirebase() {
        val nama = etNamaDaftar.text.toString().trim()
        val email = etEmailDaftar.text.toString().trim()
        val password = etPasswordDaftar.text.toString().trim()

        // Mengambil pilihan Role dari RadioButton yang dicentang
        val roleTerpilihId = rgRole.checkedRadioButtonId

        // SINKRONISASI LOGIKA: Menggunakan ID rbPenghuniRegister sesuai XML kamu
        val roleInput = if (roleTerpilihId == R.id.rbPenghuniRegister) {
            "PENGHUNI"
        } else {
            "PEMILIK"
        }

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter ya!", Toast.LENGTH_SHORT).show()
            return
        }

        btnDaftar.text = "Mendaftarkan..."
        btnDaftar.isEnabled = false

        // 1. Daftarkan Email & Password ke Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnDaftar.text = "Daftar Akun"
                btnDaftar.isEnabled = true

                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    // 2. Siapkan data yang akan disimpan ke Realtime Database
                    val userMap = HashMap<String, Any>()
                    userMap["uid"] = uid
                    userMap["nama"] = nama
                    userMap["email"] = email
                    userMap["role"] = roleInput

                    // 3. Simpan data ke node "pengguna" -> "UID" di Realtime Database
                    val dbRef = FirebaseDatabase.getInstance().getReference("pengguna").child(uid)

                    dbRef.setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                            // Kembali otomatis ke halaman login setelah registrasi sukses
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal menyimpan ke database: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}