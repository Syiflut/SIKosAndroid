package com.kelompok5.sikos.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RoomChat(
    var roomId: String = "",
    var penghuniId: String = "",
    var pemilikId: String = "",
    var namaPenerima: String = "",
    var pesanTerakhir: String = "",
    var waktu: Long = 0L,
    var role: String = ""
)