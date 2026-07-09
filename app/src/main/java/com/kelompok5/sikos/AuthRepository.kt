package com.kelompok5.sikos.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelompok5.sikos.core.data.local.entity.Pengguna
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) {
    suspend fun login(email: String, pass: String): Result<Pengguna> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid ?: throw Exception("User not found")
            val snapshot = db.getReference("pengguna").child(uid).get().await()
            val pengguna = snapshot.getValue(Pengguna::class.java)!!
            Result.success(pengguna)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(pengguna: Pengguna, pass: String): Result<Unit> {
        // 1. Bikin akun di Firebase Authentication
        val result = auth.createUserWithEmailAndPassword(pengguna.email, pass).await()
        val uid = result.user?.uid!!

        // 2. Simpen data lengkapnya ke Realtime Database
        val dataBaru = pengguna.copy(uid = uid)
        db.getReference("pengguna").child(uid).setValue(dataBaru).await()

        return Result.success(Unit)
    }
    }
