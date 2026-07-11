package com.kelompok5.sikos.model

// Menggunakan data class agar otomatis memiliki fungsi toString(), equals(), dll.
data class Chat(
    val idChat: String,
    val namaPenerima: String,
    val pesanTerakhir: String,
    val waktu: String,
    val tipeUser: String // Untuk membedakan status pengguna ("Pemilik" atau "Penghuni")
)