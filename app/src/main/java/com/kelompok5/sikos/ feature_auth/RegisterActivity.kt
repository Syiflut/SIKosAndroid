package com.kelompok5.sikos.feature_auth

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

        btnDaftar.setOnClickListener {
            prosesDaftarFirebase()
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