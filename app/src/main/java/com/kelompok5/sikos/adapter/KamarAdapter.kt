package com.kelompok5.sikos.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.FavoritManager
import com.kelompok5.sikos.model.Kamar
import com.kelompok5.sikos.ui.RoomChatActivity

class KamarAdapter(
    private val listKamar: List<Kamar>,
    private val onItemClick: (Kamar) -> Unit
) : RecyclerView.Adapter<KamarAdapter.KamarViewHolder>() {

    class KamarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKos: TextView = view.findViewById(R.id.tvNamaKos)
        val tvLokasi: TextView = view.findViewById(R.id.tvLokasi)
        val tvHargaKos: TextView = view.findViewById(R.id.tvHargaKos)
        val tvTipeKos: TextView = view.findViewById(R.id.tvTipeKos)
        val ivChatKamar: ImageView = view.findViewById(R.id.ivChatKamar)
        val ivLikeKamar: ImageView = view.findViewById(R.id.ivLikeKamar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KamarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kamar_kos, parent, false)
        return KamarViewHolder(view)
    }

    override fun onBindViewHolder(holder: KamarViewHolder, position: Int) {
        val kamar = listKamar[position]
        holder.tvNamaKos.text = kamar.namaKos
        holder.tvLokasi.text = kamar.lokasi
        holder.tvHargaKos.text = "Rp ${kamar.hargaSewa} / bulan"
        holder.tvTipeKos.text = kamar.tipeKamar

        val context = holder.itemView.context

        if (kamar.urlFoto.isNotEmpty()) {
            Glide.with(context)
                .load(kamar.urlFoto)
                .into(holder.ivChatKamar)
        }

        val isFavorit = FavoritManager.listFavorit.any { it.namaKos == kamar.namaKos }
        if (isFavorit) {
            holder.ivLikeKamar.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            holder.ivLikeKamar.setImageResource(android.R.drawable.btn_star_big_off)
        }

        holder.itemView.setOnClickListener { onItemClick(kamar) }

        holder.ivChatKamar.setOnClickListener {
            val intent = Intent(context, RoomChatActivity::class.java).apply {
                putExtra("NAMA_KOS", kamar.namaKos)
                putExtra("OWNER_UID", kamar.ownerUid)
            }
            context.startActivity(intent)
        }

        holder.ivLikeKamar.setOnClickListener {
            val sudahAda = FavoritManager.listFavorit.any { it.namaKos == kamar.namaKos }
            if (sudahAda) {
                FavoritManager.listFavorit.removeAll { it.namaKos == kamar.namaKos }
                holder.ivLikeKamar.setImageResource(android.R.drawable.btn_star_big_off)
                Toast.makeText(context, "${kamar.namaKos} dihapus dari disukai", Toast.LENGTH_SHORT).show()
            } else {
                FavoritManager.listFavorit.add(kamar)
                holder.ivLikeKamar.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(context, "${kamar.namaKos} ditambahkan ke disukai!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = listKamar.size
}