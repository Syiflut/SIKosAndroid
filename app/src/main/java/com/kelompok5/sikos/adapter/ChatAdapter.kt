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

class ChatAdapter(
    private val chatList: MutableList<Chat>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)

        return ChatViewHolder(view)

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val chat = chatList[position]

        holder.tvSender.text = chat.senderName
        holder.tvMessage.text = chat.message

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.tvTime.text = sdf.format(Date(chat.timestamp))

    }

    override fun getItemCount(): Int = chatList.size

}