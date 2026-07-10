package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kelompok5.sikos.R

class ProfilFragment : Fragment() {

    private lateinit var etPasswordLama: EditText
    private lateinit var etPasswordBaru: EditText
    private lateinit var btnSimpanPassword: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menyambungkan logika dengan layout fragment_profil_penghuni.xml
        val view = inflater.inflate(R.layout.fragment_profil_penghuni, container, false)

        etPasswordLama = view.findViewById(R.id.etPasswordLama)
        etPasswordBaru = view.findViewById(R.id.etPasswordBaru)
        btnSimpanPassword = view.findViewById(R.id.btnSimpanPassword)

        btnSimpanPassword.setOnClickListener {
            val passLama = etPasswordLama.text.toString().trim()
            val passBaru = etPasswordBaru.text.toString().trim()

            if (passLama.isEmpty() || passBaru.isEmpty()) {
                Toast.makeText(context, "Semua kolom password harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (passBaru.length < 6) {
                Toast.makeText(context, "Password baru minimal 6 karakter ya!", Toast.LENGTH_SHORT).show()
            } else {
                // Sisi ini nanti siap dihubungkan ke fitur Firebase Auth ganti password oleh teman timmu
                Toast.makeText(context, "Kata sandi berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                etPasswordLama.text.clear()
                etPasswordBaru.text.clear()
            }
        }

        return view
    }
}