package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Kamar

class KamarAdapter(
    private val listKamar: List<Kamar>,
    private val onItemClick: (Kamar) -> Unit
) : RecyclerView.Adapter<KamarAdapter.KamarViewHolder>() {

    class KamarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKos: TextView = view.findViewById(R.id.tvNamaKos)
        val tvLokasi: TextView = view.findViewById(R.id.tvLokasi)
        val tvHargaKos: TextView = view.findViewById(R.id.tvHargaKos)
        val tvTipeKos: TextView = view.findViewById(R.id.tvTipeKos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KamarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kamar_kos, parent, false)
        return KamarViewHolder(view)
    }

    override fun onBindViewHolder(holder: KamarViewHolder, position: Int) {
        val kamar = listKamar[position]
        holder.tvNamaKos.text = kamar.namaKos
        holder.tvLokasi.text = kamar.lokasi
        holder.tvHargaKos.text = "Rp ${kamar.hargaSewa.toInt()} / bulan"
        holder.tvTipeKos.text = kamar.tipeKamar

        // Aksi ketika kartu kosan diklik oleh penghuni, langsung memicu perpindahan halaman
        holder.itemView.setOnClickListener { onItemClick(kamar) }
    }

    override fun getItemCount(): Int = listKamar.size
}