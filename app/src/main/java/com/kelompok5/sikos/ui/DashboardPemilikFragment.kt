package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kelompok5.sikos.R

class DashboardPemilikFragment : Fragment() {

    private lateinit var etNamaKos: EditText
    private lateinit var etHargaKos: EditText
    private lateinit var tvNamaPemilik: TextView

    private lateinit var btnPilihFoto: Button
    private lateinit var btnSimpanKos: Button

    // Mengubah tipe data menjadi View biasa agar fleksibel dan anti-merah
    private lateinit var menuStatusBayar: View
    private lateinit var menuBroadcastChat: View
    private lateinit var menuUlasanPenghuni: View
    private lateinit var menuFiturLainnya: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_pemilik, container, false)

        // 1. Inisialisasi komponen input teks & nama
        etNamaKos = view.findViewById(R.id.etNamaKos)
        etHargaKos = view.findViewById(R.id.etHargaKos)
        tvNamaPemilik = view.findViewById(R.id.tvNamaPemilik)

        // 2. Inisialisasi Tombol Utama
        btnPilihFoto = view.findViewById(R.id.btnPilihFoto)
        btnSimpanKos = view.findViewById(R.id.btnSimpanKos)

        // 3. Inisialisasi Grid Menu bawah menggunakan View biasa
        menuStatusBayar = view.findViewById(R.id.menuStatusBayar)
        menuBroadcastChat = view.findViewById(R.id.menuBroadcastChat)

        // Menggunakan try-catch atau alternatif aman jika ID ulasan sedikit berbeda di XML kelompokmu
        menuUlasanPenghuni = view.findViewById(R.id.menuUlasanPenghuni) ?: view.findViewById(android.R.id.content)
        menuFiturLainnya = view.findViewById(R.id.menuFiturLainnya)

        // 4. Ambil data nama pemilik dari Firebase login
        val namaPemilik = arguments?.getString("KEY_NAMA") ?: "Owner"
        tvNamaPemilik.text = "Halo, $namaPemilik! (Owner)"

        // =========================================================================
        // AKSI KLIK TOMBOL & MENU (RESPONS LOGIKA BACKEND)
        // =========================================================================

        btnPilihFoto.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Galeri HP...", Toast.LENGTH_SHORT).show()
        }

        btnSimpanKos.setOnClickListener {
            val namaKos = etNamaKos.text.toString().trim()
            val hargaKos = etHargaKos.text.toString().trim()

            if (namaKos.isNotEmpty() && hargaKos.isNotEmpty()) {
                Toast.makeText(requireContext(), "Menyimpan Iklan: $namaKos | Rp $hargaKos", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Semua data form kamar wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        menuStatusBayar.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Status Bayar Penghuni", Toast.LENGTH_SHORT).show()
        }

        menuBroadcastChat.setOnClickListener {
            Toast.makeText(requireContext(), "Mengirim Pesan Broadcast Massal", Toast.LENGTH_SHORT).show()
        }

        menuUlasanPenghuni.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Daftar Review & Bintang Kos", Toast.LENGTH_SHORT).show()
        }

        menuFiturLainnya.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Pengaturan Properti", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}