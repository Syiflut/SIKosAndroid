package com.kelompok5.sikos.feature_auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast
private val repository = AuthRepository()

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnRegister.setOnClickListener {

            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val role = if (binding.rbPemilik.isChecked)
                "pemilik"
            else
                "penghuni"

            repository.register(
                nama,
                email,
                password,
                role,
                {
                    Toast.makeText(
                        this,
                        "Registrasi Berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                },
                {

                    Toast.makeText(
                        this,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            )

        }

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}