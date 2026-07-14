package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.RoomChat

class ListChatAdapter(
    private val listRoom: List<RoomChat>,
    private val onItemClick: (RoomChat) -> Unit
) : RecyclerView.Adapter<ListChatAdapter.ListChatViewHolder>() {

    class ListChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKos: TextView = view.findViewById(R.id.tvNamaKos)
        val tvPesanTerakhir: TextView = view.findViewById(R.id.tvLokasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kamar_kos, parent, false)
        return ListChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListChatViewHolder, position: Int) {
        val room = listRoom[position]
        holder.tvNamaKos.text = room.namaPenerima
        holder.tvPesanTerakhir.text = room.pesanTerakhir

        holder.itemView.setOnClickListener {
            onItemClick(room)
        }
    }

    override fun getItemCount(): Int = listRoom.size
}