package com.kelompok5.sikos

// 1. BASE CLASS: PENGGUNA
open class Pengguna(
    val idUser: String,
    val nama: String,
    val noHp: String,
    val password: String,
    val role: String // "PEMILIK" atau "PENGHUNI"
)

// 2. CHILD CLASS: PEMILIK
class Pemilik(
    idUser: String,
    nama: String,
    noHp: String,
    password: String,
    var noRekening: String
) : Pengguna(idUser, nama, noHp, password, "PEMILIK")

// 3. CHILD CLASS: PENGHUNI
class Penghuni(
    idUser: String,
    nama: String,
    noHp: String,
    password: String,
    var tglMasuk: String
) : Pengguna(idUser, nama, noHp, password, "PENGHUNI")

// 4. ENTITY: PROPERTI KOS
data class PropertiKos(
    val idProperti: String,
    val namaKos: String,
    val alamat: String,
    val kota: String,
    val idPemilik: String
)

// 5. ENTITY: KAMAR KOS
data class Kamar(
    val idKamar: String,
    val idProperti: String,
    val nomorKamar: String,
    val hargaSewa: Double,
    val fasilitas: String,
    var statusKamar: String // "tersedia" atau "dipesan"
)