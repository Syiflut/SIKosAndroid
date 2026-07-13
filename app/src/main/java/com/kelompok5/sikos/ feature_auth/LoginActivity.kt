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

            // Mengambil ID Radio Button yang dipilih saat login (Penghuni / Pemilik)
            val roleTerpilihId = rgRole.checkedRadioButtonId
            val roleInput = if (roleTerpilihId == R.id.rbPenghuni) "PENGHUNI" else "PEMILIK"

            if (email.isNotEmpty() && password.isNotEmpty() &&
                tvErrorEmail.visibility == View.GONE && tvErrorPassword.visibility == View.GONE) {

                // 1. Proses Login menggunakan Firebase Authentication
                val mAuth = com.google.firebase.auth.FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = mAuth.currentUser?.uid ?: ""

                            // 2. Ambil data detail user dari Firebase Realtime Database berdasarkan UID
                            val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance()
                                .getReference("users").child(uid)

                            dbRef.get().addOnSuccessListener { snapshot ->
                                if (snapshot.exists()) {
                                    // Ambil data role asli yang terdaftar di database
                                    val roleDatabase = snapshot.child("role").value.toString()
                                    val namaAsli = snapshot.child("nama").value.toString()

                                    // 3. Validasi: Apakah Role yang dipilih saat login SAMA dengan yang ada di Database?
                                    if (roleInput == roleDatabase) {
                                        Toast.makeText(this, "Berhasil masuk sebagai $roleDatabase", Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.putExtra("EXTRA_NAMA", namaAsli)
                                        intent.putExtra("EXTRA_ROLE", roleDatabase) // Mengirim "PENGHUNI" atau "PEMILIK"
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Jika memilih Pemilik tapi di DB terdaftarnya Penghuni
                                        Toast.makeText(this, "Akun Anda terdaftar sebagai $roleDatabase, bukan $roleInput!", Toast.LENGTH_LONG).show()
                                        mAuth.signOut()
                                    }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "Gagal mengambil data dari database", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            // Jika email atau password salah
                            Toast.makeText(this, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
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