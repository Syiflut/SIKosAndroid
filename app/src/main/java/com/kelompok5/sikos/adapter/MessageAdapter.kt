package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Chat
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val listPesan: List<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MINE = 1
    private val VIEW_TYPE_OTHER = 2
    private val myUserId = "user123" // Nanti ganti FirebaseAuth.getInstance().currentUser?.uid

    override fun getItemViewType(position: Int): Int {
        return if (listPesan[position].senderId == myUserId) VIEW_TYPE_MINE else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MINE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_mine, parent, false)
            MineViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_other, parent, false)
            OtherViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = listPesan[position]
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(chat.timestamp))

        if (holder is MineViewHolder) {
            holder.tvPesan.text = chat.message
            holder.tvWaktu.text = time
        } else if (holder is OtherViewHolder) {
            holder.tvPesan.text = chat.message
            holder.tvWaktu.text = time
        }
    }

    override fun getItemCount(): Int = listPesan.size

    class MineViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvPesan: TextView = v.findViewById(R.id.tvPesanMine)
        val tvWaktu: TextView = v.findViewById(R.id.tvWaktuMine)
    }

    class OtherViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvPesan: TextView = v.findViewById(R.id.tvPesanOther)
        val tvWaktu: TextView = v.findViewById(R.id.tvWaktuOther)
    }
}