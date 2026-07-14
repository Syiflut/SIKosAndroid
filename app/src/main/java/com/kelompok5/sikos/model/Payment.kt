package com.kelompok5.sikos.model

data class Payment(
    var id: String = "",
    var penghuniId: String = "",
    var namaPenghuni: String = "",
    var jumlah: Double = 0.0,
    var imageUrl: String = "",
    var status: String = "MENUNGGU",
    var tanggal: Long = System.currentTimeMillis()
)