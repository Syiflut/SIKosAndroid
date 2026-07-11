package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.RiwayatAdapter
import com.kelompok5.sikos.model.Riwayat

class RiwayatFragment : Fragment() {

    private lateinit var rvRiwayatPembayaran: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menyambungkan dengan layout fragment_riwayat_penghuni.xml
        val view = inflater.inflate(R.layout.fragment_riwayat_penghuni, container, false)

        rvRiwayatPembayaran = view.findViewById(R.id.rvRiwayatPembayaran)
        rvRiwayatPembayaran.layoutManager = LinearLayoutManager(context)

        // Data dummy transaksi untuk simulasi awal
        val listDummyRiwayat = listOf(
            Riwayat("TX001", "Kos Alinda", "15 Mar 2026", 1950000.0, 3, "Lunas"),
            Riwayat("TX002", "Kos Syifa", "10 Apr 2026", 800000.0, 1, "Menunggu")
        )

        // Pasang adapter ke RecyclerView riwayat
        val adapter = RiwayatAdapter(listDummyRiwayat)
        rvRiwayatPembayaran.adapter = adapter

        return view
    }
}