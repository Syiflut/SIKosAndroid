package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.ChatAdapter
import com.kelompok5.sikos.model.Chat

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    private val chatList = mutableListOf<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.rvChat)
        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)

        adapter = ChatAdapter(chatList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        btnSend.setOnClickListener {

            val pesan = etMessage.text.toString()

            if (pesan.isNotEmpty()) {

                val chat = Chat(
                    senderName = "Penghuni",
                    message = pesan
                )

                chatList.add(chat)
                adapter.notifyItemInserted(chatList.size - 1)

                recyclerView.scrollToPosition(chatList.size - 1)

                etMessage.text.clear()
            }
        }

        return view
    }
}