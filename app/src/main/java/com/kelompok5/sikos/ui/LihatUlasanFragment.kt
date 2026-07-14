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
import com.kelompok5.sikos.adapter.ReviewAdapter
import com.kelompok5.sikos.model.Review

class LihatUlasanFragment : Fragment() {

    private lateinit var rvLihatUlasan: RecyclerView
    private lateinit var tvUlasanKosongPemilik: TextView
    private val listReview = ArrayList<Review>()
    private lateinit var reviewAdapter: ReviewAdapter

    private val databaseRef = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("reviews")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lihat_ulasan, container, false)

        rvLihatUlasan = view.findViewById(R.id.rvLihatUlasan)
        tvUlasanKosongPemilik = view.findViewById(R.id.tvUlasanKosongPemilik)

        rvLihatUlasan.layoutManager = LinearLayoutManager(requireContext())
        reviewAdapter = ReviewAdapter(listReview)
        rvLihatUlasan.adapter = reviewAdapter

        muatSemuaUlasanFirebase()

        return view
    }

    private fun muatSemuaUlasanFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listReview.clear()
                // Melakukan iterasi mengambil seluruh review dari tiap kos properti
                for (kosSnapshot in snapshot.children) {
                    for (reviewSnapshot in kosSnapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if (review != null) {
                            listReview.add(review)
                        }
                    }
                }

                reviewAdapter.notifyDataSetChanged()

                if (listReview.isEmpty()) {
                    tvUlasanKosongPemilik.visibility = View.VISIBLE
                    rvLihatUlasan.visibility = View.GONE
                } else {
                    tvUlasanKosongPemilik.visibility = View.GONE
                    rvLihatUlasan.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat ulasan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}