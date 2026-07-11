package com.kelompok5.sikos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kelompok5.sikos.R
import com.kelompok5.sikos.feature_auth.LoginActivity

class ProfilFragment : Fragment() {

    private lateinit var etPasswordLama: EditText
    private lateinit var etPasswordBaru: EditText
    private lateinit var btnSimpanPassword: Button

    // TAMBAHAN: Variabel global untuk menampung tombol logout baru
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menyambungkan logika dengan layout fragment_profil_penghuni.xml
        val view = inflater.inflate(R.layout.fragment_profil_penghuni, container, false)

        etPasswordLama = view.findViewById(R.id.etPasswordLama)
        etPasswordBaru = view.findViewById(R.id.etPasswordBaru)
        btnSimpanPassword = view.findViewById(R.id.btnSimpanPassword)

        // TAMBAHAN: Menghubungkan variabel dengan ID button dari XML layout
        btnLogout = view.findViewById(R.id.btnLogout)

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

        // =======================================================
        // TAMBAHAN: Logika ketika Tombol Keluar Akun (Logout) Ditekan
        // =======================================================
        btnLogout.setOnClickListener {
            Toast.makeText(context, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

            val intent = Intent(activity, LoginActivity::class.java)

            // Menghapus semua tumpukan halaman sebelumnya agar aman dan tidak bisa di-back
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}