package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kelompok5.sikos.R

class ChatPemilikFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan fragment Kotlin ini langsung ke layout XML chat milikmu
        return inflater.inflate(R.layout.fragment_chat_pemilik, container, false)
    }
}