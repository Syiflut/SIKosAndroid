package com.kelompok5.sikos.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengguna")
data class Pengguna(
    @PrimaryKey val uid: String = "",
    val nama: String = "",
    val email: String = "",
    val noTelp: String = "",
    val role: String = "" // "PEMILIK" atau "PENGHUNI"
)