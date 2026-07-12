package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val chatList: MutableList<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // Kalau chat kita → bubble kanan, kalau chat lawan → bubble kiri
        val layout = if (viewType == MSG_TYPE_RIGHT) R.layout.item_chat_right else R.layout.item_chat_left
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]

        // 1. Set waktu
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.tvTime.text = sdf.format(Date(chat.timestamp))

        // 2. Cek ini chat teks atau foto - POIN 2 ANGGOTA 4
        if (!chat.imageUrl.isNullOrEmpty()) {
            // Kalau ada foto: tampilin foto, hide text kalau text kosong
            holder.ivImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(chat.imageUrl).into(holder.ivImage)
            holder.tvMessage.visibility = if (chat.message.isEmpty()) View.GONE else View.VISIBLE
            holder.tvMessage.text = chat.message
        } else {
            // Kalau cuma text: hide image, tampilin text
            holder.ivImage.visibility = View.GONE
            holder.tvMessage.visibility = View.VISIBLE
            holder.tvMessage.text = chat.message
        }
    }

    override fun getItemCount(): Int = chatList.size

    override fun getItemViewType(position: Int): Int {
        // Beda-in chat kita vs chat lawan
        return if (chatList[position].senderId == currentUserId) MSG_TYPE_RIGHT else MSG_TYPE_LEFT
    }
}