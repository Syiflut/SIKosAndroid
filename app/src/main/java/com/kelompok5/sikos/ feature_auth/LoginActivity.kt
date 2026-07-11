package com.kelompok5.sikos.feature_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kelompok5.sikos.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Alur Asli: Klik tombol langsung memicu fungsi login di ViewModel
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Alur Asli: Mengamati perubahan status login dari ViewModel
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { result ->
                if (result != null) {
                    if (result.isSuccess) {
                        Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                        // Berpindah ke Halaman Utama (MainActivity)
                        val intent = Intent(this@LoginActivity, com.kelompok5.sikos.ui.MainActivity::class.java)
                        startActivity(intent)

                        viewModel.resetLoginState()
                        finish()
                    } else if (result.isFailure) {
                        val pesanError = result.exceptionOrNull()?.message ?: "Login Gagal! Periksa kembali akun Anda."
                        Toast.makeText(this@LoginActivity, pesanError, Toast.LENGTH_SHORT).show()

                        viewModel.resetLoginState()
                    }
                }
            }
        }
    }
}