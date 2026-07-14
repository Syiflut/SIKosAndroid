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
    private val database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        rvDaftarChat = view.findViewById(R.id.rvDaftarChat)
        tvChatKosong = view.findViewById(R.id.tvChatKosong)

        adapter = ObrolanAdapter(daftarObrolan) { room ->
            val intent = Intent(requireContext(), RoomChatActivity::class.java)
            intent.putExtra("EXTRA_ROOM_ID", room.roomId)
            intent.putExtra("NAMA_KOS", room.namaPenerima)
            intent.putExtra("OWNER_UID", room.pemilikId)
            startActivity(intent)
        }

        rvDaftarChat.layoutManager = LinearLayoutManager(requireContext())
        rvDaftarChat.adapter = adapter
        loadDataChatDariFirebase()
        return view
    }

    private fun loadDataChatDariFirebase() {
        if (currentUid.isEmpty()) return
        val ref = database.getReference("userRooms")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                daftarObrolan.clear()
                for (roomSnap in snapshot.children) {
                    val penghuniId = roomSnap.child("penghuniId").value.toString().trim()
                    if (penghuniId.equals(currentUid, ignoreCase = true)) {
                        val room = roomSnap.getValue(RoomChat::class.java)
                        if (room != null) {
                            daftarObrolan.add(room)
                        }
                    }
                }
                daftarObrolan.sortByDescending { it.waktu }
                adapter.notifyDataSetChanged()

                if (daftarObrolan.isEmpty()) {
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
        private val onClick: (RoomChat) -> Unit
    ) : RecyclerView.Adapter<ObrolanAdapter.ChatViewHolder>() {

        class ChatViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val nama: TextView = v.findViewById(R.id.tvNamaKontak)
            val pesan: TextView = v.findViewById(R.id.tvPesanTerakhir)
            val waktu: TextView = v.findViewById(R.id.tvWaktuChat)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            return ChatViewHolder(view)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val item = list[position]
            holder.nama.text = item.namaPenerima
            holder.pesan.text = item.pesanTerakhir
            holder.waktu.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(item.waktu))
            holder.itemView.setOnClickListener { onClick(item) }
        }
    }
}