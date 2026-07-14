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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.BookingKosActivity
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.KamarAdapter
import com.kelompok5.sikos.model.Kamar

class DashboardFragment : Fragment() {

    private lateinit var rvKamarKosong: RecyclerView
    private lateinit var kamarAdapter: KamarAdapter
    private var listSemuaKos = ArrayList<Kamar>()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("kos_properties")

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_penghuni, container, false)

        val namaDiterima = arguments?.getString("KEY_NAMA") ?: "Pengguna"
        val tvHaloUser = view.findViewById<TextView>(R.id.tvHaloUser)
        tvHaloUser.text = "Halo, $namaDiterima!"

        rvKamarKosong = view.findViewById(R.id.rvKamarKosong)
        rvKamarKosong.layoutManager = LinearLayoutManager(context)

        kamarAdapter = KamarAdapter(ArrayList()) { kamar ->
            val intent = Intent(activity, BookingKosActivity::class.java)
            startActivity(intent)
        }
        rvKamarKosong.adapter = kamarAdapter

        ambilDataDariFirebase()

        val semuaGambar = ArrayList<ImageView>()
        cariSemuaImageView(view, semuaGambar)

        if (semuaGambar.isNotEmpty()) {
            val btnCariOtomatis = semuaGambar[0]

            btnCariOtomatis.setOnClickListener {
                val filterDialog = FilterFragment { alamat, tipe, rating, minHarga, maxHarga ->

                    val dataHasilFilter = listSemuaKos.filter { kamar ->
                        val cocokAlamat = alamat == "Semua Lokasi" || kamar.lokasi.contains(alamat, ignoreCase = true)
                        val cocokTipe = tipe == "Campur" || kamar.tipeKamar.equals(tipe, ignoreCase = true)

                        val hargaDouble = kamar.hargaSewa.toDoubleOrNull() ?: 0.0
                        val cocokHarga = hargaDouble >= minHarga && hargaDouble <= maxHarga

                        cocokAlamat && cocokTipe && cocokHarga
                    }

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

    private fun ambilDataDariFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listSemuaKos.clear()
                if (snapshot.exists()) {
                    for (kosSnapshot in snapshot.children) {
                        val namaKos = kosSnapshot.child("namaKos").value.toString()
                        val hargaKos = kosSnapshot.child("hargaKos").value.toString()
                        val urlFoto = kosSnapshot.child("urlFoto").value.toString()
                        val status = kosSnapshot.child("status").value.toString()

                        val lokasiAsli = kosSnapshot.child("lokasi").value?.toString() ?: "Bandung"

                        var ownerUid = kosSnapshot.child("ownerUid").value.toString()
                        if (ownerUid == "null" || ownerUid.trim().isEmpty()) {
                            ownerUid = kosSnapshot.child(" ownerUid").value.toString()
                        }

                        val kamar = Kamar(
                            noKamar = "1",
                            tipeKamar = "Campur",
                            hargaSewa = hargaKos,
                            statusKamar = status,
                            namaKos = namaKos,
                            lokasi = lokasiAsli,
                            fasilitas = "Lengkap",
                            urlFoto = urlFoto,
                            ownerUid = ownerUid
                        )
                        listSemuaKos.add(kamar)
                    }
                    perbaruiTampilanDaftarKos(listSemuaKos)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

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