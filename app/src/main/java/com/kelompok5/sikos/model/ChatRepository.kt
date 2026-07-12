package com.kelompok5.sikos.model

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ChatRepository {
    private val db = FirebaseDatabase.getInstance()
    private val chatRef = db.getReference("message")
    private val storageRef = FirebaseStorage.getInstance().reference

    // 1. FUNGSI KIRIM CHAT TEXT - Buat Poin 1 Realtime Chat
    fun sendMessage(roomId: String, chat: Chat, onComplete: (Boolean) -> Unit) {
        val messageId = chatRef.child(roomId).push().key ?: return
        chat.id = messageId
        chatRef.child(roomId).child(messageId).setValue(chat)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // 2. FUNGSI KIRIM FOTO - Buat Poin 2 Kirim Foto di Chat
    fun sendImageMessage(roomId: String, senderId: String, receiverId: String, imageUri: Uri, onComplete: (Boolean) -> Unit) {
        val fileName = "chat_images/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val chat = Chat(
                        senderId = senderId,
                        receiverId = receiverId,
                        message = "",
                        imageUrl = uri.toString(),
                        timestamp = System.currentTimeMillis()
                    )
                    sendMessage(roomId, chat, onComplete)
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

    // 3. FUNGSI AMBIL CHAT REALTIME - Buat Poin 1 Realtime Chat
    fun getMessages(roomId: String) = chatRef.child(roomId).orderByChild("timestamp")
}