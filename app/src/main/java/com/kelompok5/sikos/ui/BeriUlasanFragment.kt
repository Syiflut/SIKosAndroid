package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Review

class BeriUlasanFragment : Fragment() {

    private lateinit var ratingBarKos: RatingBar
    private lateinit var etUlasanInput: EditText
    private lateinit var btnKirimUlasan: Button

    private val database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val auth = FirebaseAuth.getInstance()

    private var kosId: String = "KOS_DEFAULT_01"
    private var namaPenghuni: String = "Penghuni Sikos"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beri_ulasan, container, false)

        arguments?.let {
            kosId = it.getString("KOS_ID", "KOS_DEFAULT_01")
            namaPenghuni = it.getString("NAMA_PENGHUNI", "Penghuni Sikos")
        }

        ratingBarKos = view.findViewById(R.id.ratingBarKos)
        etUlasanInput = view.findViewById(R.id.etUlasanInput)
        btnKirimUlasan = view.findViewById(R.id.btnKirimUlasan)

        btnKirimUlasan.setOnClickListener {
            prosesKirimUlasan()
        }

        return view
    }

    private fun prosesKirimUlasan() {
        val ratingNilai = ratingBarKos.rating
        val teksUlasan = etUlasanInput.text.toString().trim()
        val currentUid = auth.currentUser?.uid ?: ""

        if (ratingNilai == 0.0f) {
            Toast.makeText(requireContext(), "Silakan tentukan bintang rating terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val refReview = database.getReference("reviews").child(kosId).push()
        val reviewIdUnique = refReview.key ?: ""

        val ulasanBaru = Review(
            reviewId = reviewIdUnique,
            kosId = kosId,
            penghuniId = currentUid,
            namaPenghuni = namaPenghuni,
            rating = ratingNilai,
            ulasanText = teksUlasan,
            timestamp = System.currentTimeMillis()
        )

        refReview.setValue(ulasanBaru)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Ulasan Anda berhasil terkirim!", Toast.LENGTH_SHORT).show()
                etUlasanInput.setText("")
                ratingBarKos.rating = 0.0f
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Gagal mengirim ulasan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}