package com.kelompok5.sikos.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RoomChat(
    val roomId: String = "",
    val penghuniId: String = "",
    val pemilikId: String = "",
    val namaPenerima: String = "",
    val pesanTerakhir: String = "",
    val waktu: Long = 0L, // Pake Long biar bisa di-sort
    val role: String = ""
)