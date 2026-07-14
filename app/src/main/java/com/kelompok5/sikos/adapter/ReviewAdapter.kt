package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Review

class ReviewAdapter(private val listReview: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama: TextView = v.findViewById(R.id.tvNamaPengulas)
        val tvIsi: TextView = v.findViewById(R.id.tvIsiUlasan)
        val ratingBar: RatingBar = v.findViewById(R.id.ratingBarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listReview[position]
        holder.tvNama.text = data.namaPenghuni
        holder.tvIsi.text = data.ulasanText
        holder.ratingBar.rating = data.rating
    }

    override fun getItemCount(): Int = listReview.size
}