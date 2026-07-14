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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.MessageAdapter
import com.kelompok5.sikos.model.Chat

class RoomChatActivity : AppCompatActivity() {

    private lateinit var tvNamaRoomChat: TextView
    private lateinit var etPesanInput: EditText
    private lateinit var btnKirimPesan: Button
    private lateinit var rvPesan: RecyclerView
    private lateinit var tvChatKosong: TextView

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var roomChatId: String? = null
    private var namaKos: String? = null
    private var ownerUid: String? = null
    private val listPesan = ArrayList<Chat>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        val currentUid = auth.currentUser?.uid ?: ""

        roomChatId = intent.getStringExtra("EXTRA_ROOM_ID")
        ownerUid = intent.getStringExtra("OWNER_UID")?.trim()
        namaKos = intent.getStringExtra("NAMA_KOS")

        if (roomChatId.isNullOrEmpty() && !ownerUid.isNullOrEmpty() && ownerUid != "null") {
            roomChatId = "${currentUid}_${ownerUid}"
        }

        tvNamaRoomChat = findViewById(R.id.tvNamaRoomChat)
        etPesanInput = findViewById(R.id.etPesanInput)
        btnKirimPesan = findViewById(R.id.btnKirimPesan)
        rvPesan = findViewById(R.id.rvPesan)
        tvChatKosong = findViewById(R.id.tvChatKosong)

        tvNamaRoomChat.text = namaKos ?: "Chat Percakapan"

        rvPesan.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(listPesan)
        rvPesan.adapter = messageAdapter

        muatPesanDariFirebase()

        btnKirimPesan.setOnClickListener {
            kirimPesanKeFirebase(currentUid)
        }
    }

    private fun muatPesanDariFirebase() {
        if (roomChatId == null) return

        database.child("chats").child(roomChatId!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listPesan.clear()
                    for (data in snapshot.children) {
                        val pesan = data.getValue(Chat::class.java)
                        if (pesan != null) {
                            listPesan.add(pesan)
                        }
                    }

                    messageAdapter.notifyDataSetChanged()

                    if (listPesan.isEmpty()) {
                        tvChatKosong.visibility = View.VISIBLE
                    } else {
                        tvChatKosong.visibility = View.GONE
                        rvPesan.scrollToPosition(listPesan.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RoomChatActivity, "Gagal memuat chat: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun kirimPesanKeFirebase(pengirimId: String) {
        val teksPesan = etPesanInput.text.toString().trim()

        if (teksPesan.isEmpty() || roomChatId == null) return

        val chatRef = database.child("chats").child(roomChatId!!).child("messages").push()
        val chatIdUnique = chatRef.key ?: ""

        val pesanBaru = Chat(
            id = chatIdUnique,
            senderId = pengirimId,
            message = teksPesan,
            timestamp = System.currentTimeMillis()
        )

        chatRef.setValue(pesanBaru)
            .addOnSuccessListener {
                etPesanInput.setText("")
                updateInfoRoomUtama(teksPesan)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengirim pesan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateInfoRoomUtama(teksTerakhir: String) {
        val currentUid = auth.currentUser?.uid ?: return
        if (roomChatId.isNullOrEmpty()) return

        if ((ownerUid.isNullOrEmpty() || ownerUid == "null") && roomChatId!!.contains("_")) {
            val splitId = roomChatId!!.split("_")
            if (splitId.size > 1) {
                ownerUid = if (splitId[0] == currentUid) splitId[1] else splitId[0]
            }
        }

        val roomInfoRef = database.child("userRooms").child(roomChatId!!)

        val infoMap = hashMapOf(
            "roomId" to roomChatId!!,
            "penghuniId" to (if (currentUid == ownerUid) "" else currentUid),
            "pemilikId" to (ownerUid ?: ""),
            "namaPenerima" to (namaKos ?: "User"),
            "pesanTerakhir" to teksTerakhir,
            "waktu" to System.currentTimeMillis()
        )
        roomInfoRef.setValue(infoMap)
    }
}