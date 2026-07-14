package com.kelompok5.sikos.model

data class Kamar(
    val noKamar: String = "",
    val tipeKamar: String = "",
    val hargaSewa: String = "",
    val statusKamar: String = "Kosong",
    val namaKos: String = "",
    val lokasi: String = "",
    val fasilitas: String = "",
    val urlFoto: String = "",
    val ownerUid: String = ""
)