package com.kelompok5.sikos.feature_auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.R
import dagger.hilt.android.AndroidEntryPoint  // <-- import ini

@AndroidEntryPoint  // <-- WAJIB ADA INI
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}