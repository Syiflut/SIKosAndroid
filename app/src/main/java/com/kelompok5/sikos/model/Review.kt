package com.kelompok5.sikos.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    var reviewId: String = "",
    var kosId: String = "",
    var penghuniId: String = "",
    var namaPenghuni: String = "",
    var rating: Float = 0.0f,
    var ulasanText: String = "",
    var timestamp: Long = 0L
)