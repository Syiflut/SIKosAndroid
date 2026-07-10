package com.kelompok5.sikos.model

data class Riwayat(
    val idTransaksi: String = "",
    val namaKos: String = "",
    val tanggalBayar: String = "",
    val totalHarga: Double = 0.0,
    val durasiBulan: Int = 1,
    val statusPembayaran: String = "Menunggu" // Lunas, Menunggu, atau Gagal
)