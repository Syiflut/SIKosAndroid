package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.ChatAdapter
import com.kelompok5.sikos.model.Chat

class RoomChatActivity : AppCompatActivity() {

    private lateinit var tvNamaRoomChat: TextView
    private lateinit var etPesanInput: EditText
    private lateinit var btnKirimPesan: Button
    private lateinit var rvPesan: RecyclerView
    private lateinit var tvChatKosong: TextView

    private val listPesan = ArrayList<Chat>()
    private lateinit var chatAdapter: ChatAdapter

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Nanti diganti setelah Login Firebase selesai
    private val myUserId = "user123"

    private lateinit var roomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat)

        tvNamaRoomChat = findViewById(R.id.tvNamaRoomChat)
        etPesanInput = findViewById(R.id.etPesanInput)
        btnKirimPesan = findViewById(R.id.btnKirimPesan)
        rvPesan = findViewById(R.id.rvPesan)
        tvChatKosong = findViewById(R.id.tvChatKosong)

        val namaPemilik = intent.getStringExtra("EXTRA_NAMA_PEMILIK") ?: "Pemilik Kos"
        roomId = intent.getStringExtra("EXTRA_ROOM_ID") ?: ""

        tvNamaRoomChat.text = namaPemilik

        chatAdapter = ChatAdapter(listPesan)

        rvPesan.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rvPesan.adapter = chatAdapter

        loadPesanDariFirebase()
        setupBtnKirim()
    }

    private fun loadPesanDariFirebase() {

        if (roomId.isEmpty()) return

        val ref = database.getReference("message").child(roomId)

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listPesan.clear()

                for (msgSnap in snapshot.children) {
                    val chat = msgSnap.getValue(Chat::class.java)
                    if (chat != null) {
                        listPesan.add(chat)
                    }
                }

                if (listPesan.isEmpty()) {
                    tvChatKosong.visibility = View.VISIBLE
                } else {
                    tvChatKosong.visibility = View.GONE
                }

                chatAdapter.notifyDataSetChanged()

                if (listPesan.isNotEmpty()) {
                    rvPesan.scrollToPosition(listPesan.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@RoomChatActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupBtnKirim() {

        btnKirimPesan.setOnClickListener {

            val pesan = etPesanInput.text.toString().trim()

            if (pesan.isEmpty()) return@setOnClickListener

            if (roomId.isEmpty()) {
                Toast.makeText(this, "Room tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val msgRef = database
                .getReference("message")
                .child(roomId)
                .push()

            val chatBaru = Chat(
                id = msgRef.key ?: "",
                senderId = myUserId,
                senderName = auth.currentUser?.displayName ?: "Lina",
                receiverId = "",
                receiverName = "",
                message = pesan,
                imageUrl = null,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )

            msgRef.setValue(chatBaru)
                .addOnSuccessListener {
                    etPesanInput.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Gagal mengirim pesan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}