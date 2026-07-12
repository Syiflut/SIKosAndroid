package com.kelompok5.sikos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kelompok5.sikos.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Tangkap kiriman nama secara aman tanpa mengganggu pemanggilan fragment awal
        val namaUser = intent.getStringExtra("EXTRA_NAMA") ?: "Pengguna"

        // Buat objek fragment dan bungkus nama ke arguments
        val dashboardFragment = DashboardFragment()
        val bundle = Bundle()
        bundle.putString("KEY_NAMA", namaUser)
        dashboardFragment.arguments = bundle

        // Memuat halaman awal bawaan asli dengan membawa data nama baru
        loadFragment(dashboardFragment)

        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.menu_dashboard -> {
                    // Pastikan saat tab dashboard diklik ulang, nama tetap terbawa
                    val newDashboard = DashboardFragment()
                    val newBundle = Bundle()
                    newBundle.putString("KEY_NAMA", namaUser)
                    newDashboard.arguments = newBundle
                    newDashboard
                }
                R.id.menu_riwayat -> RiwayatFragment()
                R.id.menu_chat -> ChatFragment()
                R.id.menu_profil -> ProfilFragment()
                else -> DashboardFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}