package com.kelompok5.sikos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.KamarAdapter
import com.kelompok5.sikos.model.Kamar

class DashboardFragment : Fragment() {

    private lateinit var rvKamarKosong: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menyambungkan file logika ini dengan layout xml dashboard yang kamu buat kemarin
        val view = inflater.inflate(R.layout.fragment_dashboard_penghuni, container, false)

        rvKamarKosong = view.findViewById(R.id.rvKamarKosong)
        rvKamarKosong.layoutManager = LinearLayoutManager(context)

        // Ini data dummy / contoh awal agar langsung muncul daftar kosan ala Mamikos di layar kamu
        val listContohKos = listOf(
            Kamar("C3", "Campur", 650000.0, "Kosong", "Kos Alinda", "Garut", "WiFi, AC, Kasur"),
            Kamar("A1", "Putri", 800000.0, "Kosong", "Kos Syifa", "Bandung", "Kamar Mandi Dalam, WiFi")
        )

        // Memasang adapter untuk mengatur data kos ke dalam kartu visual pink pastel kamu
        val adapter = KamarAdapter(listContohKos) { kamar ->
            // AKSI KLIK: Jika salah satu kartu kosan diklik, otomatis pindah ke Form Booking Pengajuan Sewa!
            val intent = Intent(activity, BookingKosActivity::class.java)
            startActivity(intent)
        }
        rvKamarKosong.adapter = adapter

        return view
    }
}