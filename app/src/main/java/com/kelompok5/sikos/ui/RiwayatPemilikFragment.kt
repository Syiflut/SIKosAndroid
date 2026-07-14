package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kelompok5.sikos.R

class RiwayatPemilikFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan Kotlin ke XML riwayat pemilik yang baru dibuat
        return inflater.inflate(R.layout.fragment_riwayat_pemilik, container, false)
    }
}