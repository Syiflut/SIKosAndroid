package com.kelompok5.sikos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.KamarAdapter
import com.kelompok5.sikos.model.Kamar

class DashboardFragment : Fragment() {

    private lateinit var rvKamarKosong: RecyclerView
    private lateinit var kamarAdapter: KamarAdapter

    // Tempat menampung data asli dari database nantinya
    private var listSemuaKos = ArrayList<Kamar>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_penghuni, container, false)

        // =====================================================================
        // TAMBAHAN: Menampilkan Nama Pengguna secara Dinamis dari Halaman Login
        // =====================================================================
        val namaDiterima = arguments?.getString("KEY_NAMA") ?: "Pengguna"
        val tvHaloUser = view.findViewById<TextView>(R.id.tvHaloUser)
        tvHaloUser.text = "Halo, $namaDiterima!"

        // 1. Inisialisasi RecyclerView
        rvKamarKosong = view.findViewById(R.id.rvKamarKosong)
        rvKamarKosong.layoutManager = LinearLayoutManager(context)

        // 2. Inisialisasi Adapter Sekali Saja di Awal (Siap pakai)
        kamarAdapter = KamarAdapter(ArrayList()) { kamar ->
            val intent = Intent(activity, BookingKosActivity::class.java)
            startActivity(intent)
        }
        rvKamarKosong.adapter = kamarAdapter

        // =====================================================================
        // TEMPAT LOAD DATA DATABASE (NANTI TINGGAL DIKONEKSIKAN KE VIEWMODEL)
        // =====================================================================
        loadDataDummyAwal()

        // ==========================================
        // LOGIKA FILTER DENGAN POLA DATABASE
        // ==========================================
        val semuaGambar = ArrayList<ImageView>()
        cariSemuaImageView(view, semuaGambar)

        if (semuaGambar.isNotEmpty()) {
            val btnCariOtomatis = semuaGambar[0]

            btnCariOtomatis.setOnClickListener {
                val filterDialog = FilterFragment { alamat, tipe, rating, minHarga, maxHarga ->

                    // Proses menyaring dari list utama database (listSemuaKos)
                    val dataHasilFilter = listSemuaKos.filter { kamar ->
                        val cocokAlamat = alamat == "Semua Lokasi" || kamar.lokasi.contains(alamat, ignoreCase = true)
                        val cocokTipe = tipe == "Campur" || kamar.tipeKamar.equals(tipe, ignoreCase = true)
                        val cocokHarga = kamar.hargaSewa >= minHarga && kamar.hargaSewa <= maxHarga

                        cocokAlamat && cocokTipe && cocokHarga
                    }

                    // Terapkan data hasil filter ke adapter tanpa buat objek adapter baru
                    perbaruiTampilanDaftarKos(dataHasilFilter)

                    if (dataHasilFilter.isEmpty()) {
                        Toast.makeText(requireContext(), "Kos tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                filterDialog.show(parentFragmentManager, "FilterKosDialog")
            }
        }

        return view
    }

    /**
     * Fungsi untuk memuat data kos awal.
     * Saat database kelompokmu sudah siap, isi fungsi ini tinggal diganti dengan data dari database / ViewModel!
     */
    private fun loadDataDummyAwal() {
        listSemuaKos.clear()
        listSemuaKos.add(Kamar("C3", "Campur", 650000.0, "Kosong", "Kos Alinda", "Garut", "WiFi, AC, Kasur"))
        listSemuaKos.add(Kamar("A1", "Putri", 800000.0, "Kosong", "Kos Syifa", "Bandung", "Kamar Mandi Dalam, WiFi"))

        // Tampilkan ke layar
        perbaruiTampilanDaftarKos(listSemuaKos)
    }

    /**
     * Fungsi bersih untuk memperbarui data di dalam adapter secara aman
     */
    private fun perbaruiTampilanDaftarKos(daftarKosBaru: List<Kamar>) {
        kamarAdapter = KamarAdapter(daftarKosBaru) { kamar ->
            val intent = Intent(activity, BookingKosActivity::class.java)
            startActivity(intent)
        }
        rvKamarKosong.adapter = kamarAdapter
    }

    private fun cariSemuaImageView(view: View, daftar: ArrayList<ImageView>) {
        if (view is ImageView) {
            daftar.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                cariSemuaImageView(view.getChildAt(i), daftar)
            }
        }
    }
}