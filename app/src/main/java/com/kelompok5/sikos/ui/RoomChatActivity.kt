package com.kelompok5.sikos.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.R

class RoomChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat)

        val tvNamaRoomChat = findViewById<TextView>(R.id.tvNamaRoomChat)
        val etPesanInput = findViewById<EditText>(R.id.etPesanInput)
        val btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)

        // Tangkap nama pemilik kos yang diklik dari halaman daftar chat
        val namaPemilik = intent.getStringExtra("EXTRA_NAMA_PEMILIK") ?: "Pemilik Kos"
        tvNamaRoomChat.text = namaPemilik

        btnKirimPesan.setOnClickListener {
            val pesan = etPesanInput.text.toString().trim()
            if (pesan.isNotEmpty()) {
                Toast.makeText(this, "Pesan terkirim!", Toast.LENGTH_SHORT).show()
                etPesanInput.text.clear()
            }
        }
    }
}