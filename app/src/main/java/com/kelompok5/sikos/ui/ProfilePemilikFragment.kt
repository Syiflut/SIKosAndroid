package com.kelompok5.sikos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.feature_auth.LoginActivity
import com.kelompok5.sikos.R

class ProfilePemilikFragment : Fragment() {

    private lateinit var btnKeluar: Button
    private lateinit var tvProfilEmail: TextView
    private lateinit var tvProfilNama: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_pemilik, container, false)

        btnKeluar = view.findViewById(R.id.btnKeluar)
        tvProfilEmail = view.findViewById(R.id.tvProfilEmail)
        tvProfilNama = view.findViewById(R.id.tvProfilNama)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            tvProfilEmail.text = currentUser.email

            // Mengambil nama user secara realtime dari Realtime Database node "users"
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val namaTerdaftar = snapshot.child("nama").value.toString()
                        tvProfilNama.text = namaTerdaftar
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Gagal memuat nama: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnKeluar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(requireContext(), "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            activity?.finish()
        }

        return view
    }
}