package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.RiwayatAdapter
import com.kelompok5.sikos.model.Riwayat

class RiwayatFragment : Fragment() {

    private lateinit var rvRiwayatSewa: RecyclerView
    private lateinit var tvRiwayatKosong: TextView
    private val listRiwayat = ArrayList<Riwayat>()

    private val databaseRef = FirebaseDatabase.getInstance().getReference("bookings")
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat_penghuni, container, false)

        rvRiwayatSewa = view.findViewById(R.id.rvRiwayatSewa)
        tvRiwayatKosong = view.findViewById(R.id.tvRiwayatKosong)

        rvRiwayatSewa.layoutManager = LinearLayoutManager(requireContext())

        ambilDataRiwayatRealtime()

        return view
    }

    private fun ambilDataRiwayatRealtime() {
        if (currentUid.isEmpty()) return

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listRiwayat.clear()
                for (dataSnapshot in snapshot.children) {
                    val uuidPenghuni = dataSnapshot.child("penghuniId").value.toString()

                    if (uuidPenghuni == currentUid) {
                        val keyId = dataSnapshot.child("bookingId").value.toString()
                        val namaKosan = dataSnapshot.child("namaKos").value?.toString() ?: "Pengajuan Kos Saya"
                        val tglMasuk = dataSnapshot.child("tanggalMasuk").value.toString()
                        val statusBayar = dataSnapshot.child("status").value.toString()
                        val durasiSewa = dataSnapshot.child("durasi").value.toString()

                        val dataReal = Riwayat(
                            idTransaksi = keyId,
                            namaKos = namaKosan,
                            tanggalBayar = tglMasuk,
                            totalHarga = 0.0,
                            durasiBulan = durasiSewa.toIntOrNull() ?: 1,
                            statusPembayaran = statusBayar,
                            buktiBayarUrl = ""
                        )
                        listRiwayat.add(dataReal)
                    }
                }

                if (listRiwayat.isEmpty()) {
                    tvRiwayatKosong.visibility = View.VISIBLE
                    rvRiwayatSewa.visibility = View.GONE
                } else {
                    tvRiwayatKosong.visibility = View.GONE
                    rvRiwayatSewa.visibility = View.VISIBLE
                }

                rvRiwayatSewa.adapter = RiwayatAdapter(listRiwayat) { riwayatTerpilih ->
                    val fragmentUlasan = BeriUlasanFragment()
                    val bundle = Bundle()
                    bundle.putString("KOS_ID", riwayatTerpilih.idTransaksi)
                    bundle.putString("NAMA_PENGHUNI", "Penyewa Sikos")
                    fragmentUlasan.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentUlasan)
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat riwayat: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}