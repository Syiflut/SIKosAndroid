package com.kelompok5.sikos.core.data.local.entity

data class Pemilik(
    val pengguna: Pengguna,
    val noRekeningBank: String = ""
)