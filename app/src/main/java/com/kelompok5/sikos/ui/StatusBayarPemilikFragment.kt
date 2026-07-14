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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.PaymentAdapter

data class PaymentModel(
    val bookingId: String = "",
    val tanggalMasuk: String = "",
    val durasi: String = "",
    val status: String = ""
)

class StatusBayarPemilikFragment : Fragment() {

    private lateinit var rvKonfirmasiBayar: RecyclerView
    private lateinit var tvBayarKosong: TextView
    private val listPembayaran = ArrayList<PaymentModel>()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("bookings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disini sudah disesuaikan menggunakan activity_status_bayar_pemilik sesuai file XML kamu
        val view = inflater.inflate(R.layout.activity_status_bayar_pemilik, container, false)

        rvKonfirmasiBayar = view.findViewById(R.id.rvKonfirmasiBayar)
        tvBayarKosong = view.findViewById(R.id.tvBayarKosong)

        rvKonfirmasiBayar.layoutManager = LinearLayoutManager(requireContext())

        ambilDataPembayaranRealtime()

        return view
    }

    private fun ambilDataPembayaranRealtime() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPembayaran.clear()
                for (dataSnapshot in snapshot.children) {
                    val status = dataSnapshot.child("status").value.toString()

                    if (status.contains("Menunggu", ignoreCase = true)) {
                        val bookingId = dataSnapshot.child("bookingId").value.toString()
                        val tanggalMasuk = dataSnapshot.child("tanggalMasuk").value.toString()
                        val durasi = dataSnapshot.child("durasi").value.toString()

                        listPembayaran.add(PaymentModel(bookingId, tanggalMasuk, durasi, status))
                    }
                }

                if (listPembayaran.isEmpty()) {
                    tvBayarKosong.visibility = View.VISIBLE
                    rvKonfirmasiBayar.visibility = View.GONE
                } else {
                    tvBayarKosong.visibility = View.GONE
                    rvKonfirmasiBayar.visibility = View.VISIBLE
                }

                rvKonfirmasiBayar.adapter = PaymentAdapter(listPembayaran) { idBookingTerpilih ->
                    konfirmasiPembayaranLunas(idBookingTerpilih)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun konfirmasiPembayaranLunas(bookingId: String) {
        databaseRef.child(bookingId).child("status").setValue("Lunas")
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Pembayaran berhasil dikonfirmasi Lunas!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Gagal konfirmasi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}