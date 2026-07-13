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

        // SAMBUNGAN UTAMA: Mengambil data dari FavoritManager lokal
        kamarAdapter = KamarAdapter(FavoritManager.listFavorit) { kamar ->
            Toast.makeText(context, "Membuka detail ${kamar.namaKos}", Toast.LENGTH_SHORT).show()
        }
        rvKosFavorit.adapter = kamarAdapter

        btnKeluarAkun.setOnClickListener {
            Toast.makeText(context, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Fungsi ini bertugas merefresh daftar secara otomatis setiap kali tab profil dibuka
        if (::kamarAdapter.isInitialized) {
            kamarAdapter.notifyDataSetChanged()
        }
    }
}