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
    private val listPesan = ArrayList<Chat>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Mengambil data ID Room dari halaman sebelumnya
        roomChatId = intent.getStringExtra("EXTRA_ROOM_ID")

        // PERBAIKAN: Jika roomChatId null atau kosong karena belum dikirim dari halaman katalog,
        // otomatis pakai fallback "A1" (node database yang sudah berisi riwayat chat kamu)
        if (roomChatId.isNullOrEmpty()) {
            roomChatId = "A1"
        }

        // Mengambil nama kos dari intent, jika tidak ada default ke "Kos Syifa"
        namaKos = intent.getStringExtra("NAMA_KOS") ?: "Kos Syifa"

        // Inisialisasi Komponen Sesuai ID XML
        tvNamaRoomChat = findViewById(R.id.tvNamaRoomChat)
        etPesanInput = findViewById(R.id.etPesanInput)
        btnKirimPesan = findViewById(R.id.btnKirimPesan)
        rvPesan = findViewById(R.id.rvPesan)
        tvChatKosong = findViewById(R.id.tvChatKosong)

        // Set header nama kosan
        tvNamaRoomChat.text = namaKos

        // Setup RecyclerView & Adapter
        rvPesan.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(listPesan)
        rvPesan.adapter = messageAdapter

        // Mulai sinkronisasi pesan secara real-time dari Firebase
        muatPesanDariFirebase()

        // Aksi ketika tombol Kirim ditekan
        btnKirimPesan.setOnClickListener {
            kirimPesanKeFirebase()
        }
    }

    private fun muatPesanDariFirebase() {
        if (roomChatId == null) return

        // Membaca data secara dinamis dari folder: chats -> [roomChatId] -> messages
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

    private fun kirimPesanKeFirebase() {
        val teksPesan = etPesanInput.text.toString().trim()
        val pengirimId = auth.currentUser?.uid ?: return

        if (teksPesan.isEmpty()) return
        if (roomChatId == null) return

        // Mengirimkan pesan baru ke node dinamis sesuai room aktif
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
                etPesanInput.setText("") // Hapus inputan setelah pesan sukses dikirim
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengirim pesan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}