package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Riwayat

class RiwayatAdapter(
    private val items: List<Riwayat>,
    private val onUlasanClick: (Riwayat) -> Unit
) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNamaKos: TextView = v.findViewById(R.id.tvNamaKosBayar)
        val tvDetail: TextView = v.findViewById(R.id.tvDetailBayar)
        val tvStatus: TextView = v.findViewById(R.id.tvStatusBayar)
        val btnUlasan: Button = v.findViewById(R.id.btnUlasanPenghuni)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_pembayaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]

        holder.tvNamaKos.text = data.namaKos
        holder.tvDetail.text = "Mulai Masuk: ${data.tanggalBayar}\nDurasi: ${data.durasiBulan} Bulan"
        holder.tvStatus.text = data.statusPembayaran

        if (data.statusPembayaran.contains("Lunas", ignoreCase = true)) {
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            holder.btnUlasan.visibility = View.VISIBLE
        } else {
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#FF9800"))
            holder.btnUlasan.visibility = View.GONE
        }

        holder.btnUlasan.setOnClickListener {
            onUlasanClick(data)
        }
    }

    override fun getItemCount(): Int = items.size
}