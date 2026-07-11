package com.kelompok5.sikos.feature_auth

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

            val roleId = rgRole.checkedRadioButtonId
            val role = if (roleId == R.id.rbPenghuniRegister) "Penghuni" else "Pemilik Kos"

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Pendaftaran $role Berhasil!", Toast.LENGTH_LONG).show()
                // Kembali ke halaman login setelah berhasil mendaftar
                finish()
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