package com.kelompok5.sikos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kelompok5.sikos.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menyambungkan dengan file activity_main.xml kelompokmu
        setContentView(R.layout.activity_main)

        // Cari ID bottom navigation di activity_main.xml kamu.
        // Jika garis bawah merah, sesuaikan ID "bottom_navigation" dengan yang ada di XML-mu ya!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Tampilan awal default saat masuk (Dashboard)
        loadFragment(DashboardFragment())

        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.menu_dashboard -> DashboardFragment()
                R.id.menu_riwayat -> RiwayatFragment()
                R.id.menu_profil -> ProfilFragment()
                else -> DashboardFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Sesuaikan ID container tempat fragment muncul
            .commit()
    }
}