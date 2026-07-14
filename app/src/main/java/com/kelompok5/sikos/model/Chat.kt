package com.kelompok5.sikos.model

data class Chat(
    var id: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var receiverId: String = "",
    var receiverName: String = "",
    var message: String = "",
    var imageUrl: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false
)