package com.kelompok5.sikos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.ListChatAdapter
import com.kelompok5.sikos.model.RoomChat

class ChatPemilikFragment : Fragment() {

    private lateinit var rvDaftarChatPemilik: RecyclerView
    private lateinit var layoutChatKosong: LinearLayout
    private lateinit var listChatAdapter: ListChatAdapter
    private val listChat = ArrayList<RoomChat>()
    private val databaseRef = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("userRooms")
    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_pemilik, container, false)
        rvDaftarChatPemilik = view.findViewById(R.id.rvDaftarChatPemilik)
        layoutChatKosong = view.findViewById(R.id.layoutChatKosong)
        rvDaftarChatPemilik.layoutManager = LinearLayoutManager(requireContext())
        ambilDaftarChatMasuk()
        return view
    }

    private fun ambilDaftarChatMasuk() {
        if (currentUid.isEmpty()) return

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                for (chatSnapshot in snapshot.children) {
                    val pemilikId = chatSnapshot.child("pemilikId").value.toString().trim()
                    if (pemilikId.equals(currentUid, ignoreCase = true)) {
                        val room = chatSnapshot.getValue(RoomChat::class.java)
                        if (room != null) {
                            listChat.add(room)
                        }
                    }
                }

                if (listChat.isEmpty()) {
                    layoutChatKosong.visibility = View.VISIBLE
                    rvDaftarChatPemilik.visibility = View.GONE
                } else {
                    layoutChatKosong.visibility = View.GONE
                    rvDaftarChatPemilik.visibility = View.VISIBLE
                }

                listChatAdapter = ListChatAdapter(listChat) { chatTerpilih ->
                    val intent = Intent(requireContext(), RoomChatActivity::class.java)
                    intent.putExtra("EXTRA_ROOM_ID", chatTerpilih.roomId)
                    intent.putExtra("NAMA_KOS", chatTerpilih.namaPenerima)
                    intent.putExtra("OWNER_UID", chatTerpilih.pemilikId)
                    startActivity(intent)
                }
                rvDaftarChatPemilik.adapter = listChatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat chat: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}