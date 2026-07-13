package com.kelompok5.sikos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.RoomChat
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var rvDaftarChat: RecyclerView
    private lateinit var tvChatKosong: TextView

    private val daftarObrolan = ArrayList<RoomChat>()
    private lateinit var adapter: ObrolanAdapter

    // PERBAIKAN 1: Mengarahkan langsung ke URL Firebase regional Singapore (asia-southeast1)
    private val database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        rvDaftarChat = view.findViewById(R.id.rvDaftarChat)
        tvChatKosong = view.findViewById(R.id.tvChatKosong)

        adapter = ObrolanAdapter(daftarObrolan){ room ->
            val intent = Intent(requireContext(), RoomChatActivity::class.java)
            // Sesuaikan dengan data yang dilempar ke RoomChatActivity
            intent.putExtra("NAMA_KOS", room.namaPenerima)
            startActivity(intent)
        }

        rvDaftarChat.layoutManager = LinearLayoutManager(requireContext())
        rvDaftarChat.adapter = adapter

        loadDataChatDariFirebase()

        return view
    }

    private fun loadDataChatDariFirebase(){
        // PERBAIKAN 2: Mengubah query agar membaca node 'chats' sesuai dengan isi Firebase Console kamu
        val ref = database.getReference("chats")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                daftarObrolan.clear()

                for(roomSnap in snapshot.children) {
                    val roomId = roomSnap.key ?: "" // Mengambil ID room (contoh: "A1")

                    // Masuk ke sub-folder 'messages' untuk mengambil pesan terakhir
                    val messagesSnap = roomSnap.child("messages")

                    if (messagesSnap.exists()) {
                        var pesanTerakhirText = "Belum ada pesan"
                        var waktuTerakhir = System.currentTimeMillis()

                        // Iterasi untuk mencari pesan paling baru di dalam sub-folder
                        for (msg in messagesSnap.children) {
                            pesanTerakhirText = msg.child("message").value.toString()
                            waktuTerakhir = msg.child("timestamp").value as? Long ?: System.currentTimeMillis()
                        }

                        // Membuat objek RoomChat secara dinamis berdasarkan isi database Firebase kamu
                        val room = RoomChat(
                            roomId = roomId,
                            namaPenerima = if (roomId == "A1") "Kos Syifa" else "Pemilik Kos", // Penyesuaian nama tampil berdasarkan room
                            pesanTerakhir = pesanTerakhirText,
                            waktu = waktuTerakhir
                        )

                        daftarObrolan.add(room)
                    }
                }

                daftarObrolan.sortByDescending { it.waktu }
                adapter.notifyDataSetChanged()

                if(daftarObrolan.isEmpty()){
                    tvChatKosong.visibility = View.VISIBLE
                    rvDaftarChat.visibility = View.GONE
                } else {
                    tvChatKosong.visibility = View.GONE
                    rvDaftarChat.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    class ObrolanAdapter(
        private val list: List<RoomChat>,
        private val onClick:(RoomChat)->Unit
    ) : RecyclerView.Adapter<ObrolanAdapter.ChatViewHolder>(){

        class ChatViewHolder(v: View):RecyclerView.ViewHolder(v){
            val nama:TextView = v.findViewById(R.id.tvNamaKontak)
            val pesan:TextView = v.findViewById(R.id.tvPesanTerakhir)
            val waktu:TextView = v.findViewById(R.id.tvWaktuChat)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat,parent,false)
            return ChatViewHolder(view)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val item = list[position]

            holder.nama.text = item.namaPenerima
            holder.pesan.text = item.pesanTerakhir

            holder.waktu.text =
                SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(Date(item.waktu))

            holder.itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}