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

        // 1. Tangkap kiriman ROLE dari LoginActivity (Default-nya PENGHUNI jika tidak terdeteksi)
        val userRole = intent.getStringExtra("EXTRA_ROLE") ?: "PENGHUNI"
        val namaUser = intent.getStringExtra("EXTRA_NAMA") ?: "Pengguna"

        // 2. Tentukan halaman utama yang muncul pertama kali sesuai ROLE
        // ... di dalam onCreate MainActivity.kt ...

// Tentukan halaman utama yang muncul pertama kali sesuai ROLE
        if (userRole == "PEMILIK") {
            val dashboardPemilikFragment = DashboardPemilikFragment()
            val bundle = Bundle()
            bundle.putString("KEY_NAMA", namaUser) // Mengirim nama asli dari Firebase
            dashboardPemilikFragment.arguments = bundle
            loadFragment(dashboardPemilikFragment)
        } else {
            val dashboardFragment = DashboardFragment()
            val bundle = Bundle()
            bundle.putString("KEY_NAMA", namaUser)
            dashboardFragment.arguments = bundle
            loadFragment(dashboardFragment)
        }

        // 3. Atur Navigasi Menu Bawah agar tidak tertukar saat diklik
        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.menu_dashboard -> {
                    if (userRole == "PEMILIK") {
                        DashboardPemilikFragment()
                    } else {
                        val newDashboard = DashboardFragment()
                        val newBundle = Bundle()
                        newBundle.putString("KEY_NAMA", namaUser)
                        newDashboard.arguments = newBundle
                        newDashboard
                    }
                }
                R.id.menu_riwayat -> {
                    if (userRole == "PEMILIK") {
                        RiwayatPemilikFragment() // Membuka riwayat pendapatan pemilik kos
                    } else {
                        RiwayatFragment() // Membuka riwayat pembayaran milik penghuni bawaan temanmu
                    }
                }
                R.id.menu_chat -> {
                    if (userRole == "PEMILIK") {
                        ChatPemilikFragment() // Sekarang baris ini sudah aman & aktif!
                    } else {
                        ChatFragment() // Milik penghuni punya temanmu tetap aman
                    }
                }
                R.id.menu_profil -> {
                    if (userRole == "PEMILIK") {
                        ProfilePemilikFragment() // Membuka profil pemilik buatanmu
                    } else {
                        ProfilFragment() // Mengarah ke kelas profil penghuni bawaan temanmu
                    }
                }
                else -> if (userRole == "PEMILIK") DashboardPemilikFragment() else DashboardFragment()
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