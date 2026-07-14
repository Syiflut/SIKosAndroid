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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.KamarAdapter
import com.kelompok5.sikos.feature_auth.LoginActivity
import com.kelompok5.sikos.model.FavoritManager

class ProfilFragment : Fragment() {

    private lateinit var tvNamaProfil: TextView
    private lateinit var tvEmailProfil: TextView
    private lateinit var rvKosFavorit: RecyclerView
    private lateinit var btnKeluarAkun: Button
    private lateinit var kamarAdapter: KamarAdapter

    // Inisialisasi Firebase Auth untuk mendeteksi sesi login aktif
    private val auth = FirebaseAuth.getInstance()

    // Koneksi langsung ke URL Firebase Realtime Database regional Singapura (asia-southeast1)
    private val database = FirebaseDatabase.getInstance("https://sikosandroid-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil_penghuni, container, false)

        tvNamaProfil = view.findViewById(R.id.tvNamaProfil)
        tvEmailProfil = view.findViewById(R.id.tvEmailProfil)
        btnKeluarAkun = view.findViewById(R.id.btnKeluarAkun)

        rvKosFavorit = view.findViewById(R.id.rvKosFavorit)
        rvKosFavorit.layoutManager = LinearLayoutManager(context)

        kamarAdapter = KamarAdapter(FavoritManager.listFavorit) { kamar ->
            Toast.makeText(context, "Membuka detail ${kamar.namaKos}", Toast.LENGTH_SHORT).show()
        }
        rvKosFavorit.adapter = kamarAdapter

        // Memanggil fungsi untuk menampilkan informasi profil secara dinamis
        tampilkanDataPengguna()

        btnKeluarAkun.setOnClickListener {
            auth.signOut() // Memastikan sesi akun di Firebase Auth benar-benar Logout
            Toast.makeText(context, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    private fun tampilkanDataPengguna() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 1. Set email langsung dari sesi login
            tvEmailProfil.text = currentUser.email

            val uid = currentUser.uid
            // 2. Mengambil data dari node 'pengguna' -> [UID] -> 'nama'
            database.child("pengguna").child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Jika data ada di Realtime Database, pakai nama lengkap asli dari database
                        val namaLengkap = snapshot.child("nama").value?.toString() ?: "Pengguna SIKos"
                        tvNamaProfil.text = namaLengkap
                    } else {
                        // JIKA TIDAK ADA DI DATABASE: Langsung potong email sebelum '@' (Contoh: syiflut1)
                        val namaDariEmail = currentUser.email?.substringBefore("@") ?: "Pengguna SIKos"
                        tvNamaProfil.text = namaDariEmail
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Jika koneksi internet terganggu, pakai potongan nama dari email sebagai cadangan
                    val namaDariEmail = currentUser.email?.substringBefore("@") ?: "Pengguna SIKos"
                    tvNamaProfil.text = namaDariEmail
                }
            })
        } else {
            tvNamaProfil.text = "Belum Login"
            tvEmailProfil.text = "-"
        }
    }

    override fun onResume() {
        super.onResume()
        if (::kamarAdapter.isInitialized) {
            kamarAdapter.notifyDataSetChanged()
        }
    }
}