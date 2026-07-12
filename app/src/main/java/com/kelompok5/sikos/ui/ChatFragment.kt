package com.kelompok5.sikos.ui

<<<<<<< HEAD
=======
import android.content.Intent
>>>>>>> a2782578aacf9e806d8d9a107bd1e341c69bc1bd
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.Button
import android.widget.EditText
=======
import android.widget.TextView
import android.widget.Toast
>>>>>>> a2782578aacf9e806d8d9a107bd1e341c69bc1bd
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
<<<<<<< HEAD
import com.kelompok5.sikos.adapter.ChatAdapter
=======
>>>>>>> a2782578aacf9e806d8d9a107bd1e341c69bc1bd
import com.kelompok5.sikos.model.Chat

class ChatFragment : Fragment() {

<<<<<<< HEAD
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
=======
    private lateinit var rvDaftarChat: RecyclerView
    private lateinit var tvChatKosong: TextView
    private var daftarObrolan = ArrayList<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        rvDaftarChat = view.findViewById(R.id.rvDaftarChat)
        tvChatKosong = view.findViewById(R.id.tvChatKosong)

        rvDaftarChat.layoutManager = LinearLayoutManager(context)

        // Memuat data chat awal (Siap diganti data SQLite / Room / Firebase kelompokmu)
        loadDataChatBawaan()

        return view
    }

    private fun loadDataChatBawaan() {
        daftarObrolan.clear()

        // Data dummy chat ke pemilik kos contoh Alinda dan Syifa
        daftarObrolan.add(Chat("CH01", "Pemilik Kos Alinda", "Halo, untuk pembayaran bulan depan via transfer ya.", "19:40", "Pemilik"))
        daftarObrolan.add(Chat("CH02", "Pemilik Kos Syifa", "Kunci kamar cadangan ada di pos satpam depan.", "Kemarin", "Pemilik"))

        if (daftarObrolan.isEmpty()) {
            tvChatKosong.visibility = View.VISIBLE
            rvDaftarChat.visibility = View.GONE
        } else {
            tvChatKosong.visibility = View.GONE
            rvDaftarChat.visibility = View.VISIBLE

            // Mengeset data ke adapter internal agar lebih efisien tempat
            rvDaftarChat.adapter = ObrolanAdapter(daftarObrolan) { chat ->
                Toast.makeText(requireContext(), "Membuka obrolan dengan ${chat.namaPenerima}", Toast.LENGTH_SHORT).show()

                // Menjalankan perpindahan halaman ke RoomChatActivity
                val intent = Intent(activity, RoomChatActivity::class.java)
                intent.putExtra("EXTRA_NAMA_PEMILIK", chat.namaPenerima)
                startActivity(intent)
            }
        }
    }

    // =====================================================================
    // TIPE: CLASS BIASA (INNER ADAPTER) - Untuk mengatur data RecyclerView
    // =====================================================================
    private class ObrolanAdapter(
        private val listChat: List<Chat>,
        private val onClick: (Chat) -> Unit
    ) : RecyclerView.Adapter<ObrolanAdapter.ChatViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            return ChatViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val data = listChat[position]
            holder.nama.text = data.namaPenerima
            holder.pesan.text = data.pesanTerakhir
            holder.waktu.text = data.waktu
            holder.itemView.setOnClickListener { onClick(data) }
        }

        override fun getItemCount(): Int = listChat.size

        // TIPE: CLASS BIASA (VIEWHOLDER) - Penghubung ke objek komponen visual
        class ChatViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val nama: TextView = v.findViewById(R.id.tvNamaKontak)
            val pesan: TextView = v.findViewById(R.id.tvPesanTerakhir)
            val waktu: TextView = v.findViewById(R.id.tvWaktuChat)
        }
    }
>>>>>>> a2782578aacf9e806d8d9a107bd1e341c69bc1bd
}