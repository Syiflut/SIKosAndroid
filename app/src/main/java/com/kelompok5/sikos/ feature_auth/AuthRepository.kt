package com.kelompok5.sikos.feature_auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelompok5.sikos.model.User

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun register(
        nama: String,
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val uid = auth.currentUser!!.uid

                val user = User(
                    uid,
                    nama,
                    email,
                    role
                )

                database.child("users")
                    .child(uid)
                    .setValue(user)
                    .addOnSuccessListener {

                        onSuccess()

                    }
                    .addOnFailureListener {

                        onFailed(it.message ?: "Gagal menyimpan data")

                    }

            }
            .addOnFailureListener {

                onFailed(it.message ?: "Registrasi gagal")

            }

    }

}