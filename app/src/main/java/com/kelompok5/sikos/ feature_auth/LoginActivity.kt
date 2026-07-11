package com.kelompok5.sikos.feature_auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.R
import com.kelompok5.sikos.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val rgRole = findViewById<RadioGroup>(R.id.rgRole)
        val tvDaftarAkun = findViewById<TextView>(R.id.tvDaftarAkun)

        val tvErrorEmail = findViewById<TextView>(R.id.tvErrorEmail)
        val tvErrorPassword = findViewById<TextView>(R.id.tvErrorPassword)

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailInput = s.toString().trim()
                if (emailInput.isEmpty()) {
                    tvErrorEmail.text = "Email tidak boleh kosong"
                    tvErrorEmail.visibility = View.VISIBLE
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    tvErrorEmail.text = "Format email tidak valid (contoh: user@email.com)"
                    tvErrorEmail.visibility = View.VISIBLE
                } else {
                    tvErrorEmail.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwordInput = s.toString().trim()
                if (passwordInput.isEmpty()) {
                    tvErrorPassword.text = "Password tidak boleh kosong"
                    tvErrorPassword.visibility = View.VISIBLE
                } else if (passwordInput.length < 6) {
                    tvErrorPassword.text = "Password minimal terdiri dari 6 karakter"
                    tvErrorPassword.visibility = View.VISIBLE
                } else {
                    tvErrorPassword.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val roleTerpilihId = rgRole.checkedRadioButtonId
            val role = if (roleTerpilihId == R.id.rbPenghuni) "Penghuni" else "Pemilik Kos"

            if (email.isNotEmpty() && password.isNotEmpty() &&
                tvErrorEmail.visibility == View.GONE && tvErrorPassword.visibility == View.GONE) {

                Toast.makeText(this, "Berhasil masuk sebagai $role", Toast.LENGTH_SHORT).show()

                // Mengambil teks di depan '@' untuk nama dinamis
                val namaPanggilan = email.substringBefore("@").replaceFirstChar { it.uppercase() }

                val intent = Intent(this, MainActivity::class.java)
                // Menyisipkan data nama tanpa merusak parameter alur intent asli
                intent.putExtra("EXTRA_NAMA", namaPanggilan)

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Mohon periksa kembali inputan Anda!", Toast.LENGTH_SHORT).show()
            }
        }

        tvDaftarAkun.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}