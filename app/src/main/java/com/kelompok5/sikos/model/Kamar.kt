package com.kelompok5.sikos.model

data class Kamar(
    val noKamar: String = "",
    val tipeKamar: String = "",
    val hargaSewa: Double = 0.0,
    val statusKamar: String = "Kosong",
    val namaKos: String = "",
    val lokasi: String = "",
    val fasilitas: String = ""
)