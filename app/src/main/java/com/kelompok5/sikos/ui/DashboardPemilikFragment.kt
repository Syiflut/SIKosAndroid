package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelompok5.sikos.R

class DashboardPemilikFragment : Fragment() {

    private lateinit var etNamaKos: EditText
    private lateinit var etHargaKos: EditText
    private lateinit var tvNamaPemilik: TextView

    private lateinit var btnPilihFoto: Button
    private lateinit var btnSimpanKos: Button

    // Mengubah tipe data menjadi View biasa agar fleksibel dan anti-merah
    private lateinit var menuStatusBayar: View
    private lateinit var menuBroadcastChat: View
    private lateinit var menuUlasanPenghuni: View
    private lateinit var menuFiturLainnya: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_pemilik, container, false)

        // 1. Inisialisasi komponen input teks & nama
        etNamaKos = view.findViewById(R.id.etNamaKos)
        etHargaKos = view.findViewById(R.id.etHargaKos)
        tvNamaPemilik = view.findViewById(R.id.tvNamaPemilik)

        // 2. Inisialisasi Tombol Utama
        btnPilihFoto = view.findViewById(R.id.btnPilihFoto)
        btnSimpanKos = view.findViewById(R.id.btnSimpanKos)

        // 3. Inisialisasi Grid Menu bawah menggunakan View biasa
        menuStatusBayar = view.findViewById(R.id.menuStatusBayar)
        menuBroadcastChat = view.findViewById(R.id.menuBroadcastChat)

        // Menggunakan try-catch atau alternatif aman jika ID ulasan sedikit berbeda di XML kelompokmu
        menuUlasanPenghuni = view.findViewById(R.id.menuUlasanPenghuni) ?: view.findViewById(android.R.id.content)
        menuFiturLainnya = view.findViewById(R.id.menuFiturLainnya)

        // 4. Ambil data nama pemilik dari Firebase login
        val namaPemilik = arguments?.getString("KEY_NAMA") ?: "Owner"
        tvNamaPemilik.text = "Halo, $namaPemilik! (Owner)"

        // =========================================================================
        // AKSI KLIK TOMBOL & MENU (RESPONS LOGIKA BACKEND)
        // =========================================================================

        btnPilihFoto.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Galeri HP...", Toast.LENGTH_SHORT).show()
        }

        // Import tambahan di bagian paling atas file jika diperlukan:
// import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.database.FirebaseDatabase

        btnSimpanKos.setOnClickListener {
            val namaKos = etNamaKos.text.toString().trim()
            val hargaKos = etHargaKos.text.toString().trim()

            if (namaKos.isNotEmpty() && hargaKos.isNotEmpty()) {
                // 1. Ambil ID unik Pemilik Kos yang sedang login saat ini
                val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

                if (currentUserUid != null) {
                    // 2. Referensikan cabang baru di Firebase Database, misal namanya "kos_properties"
                    val databaseRef = FirebaseDatabase.getInstance().getReference("kos_properties")

                    // 3. Buat struktur data object yang akan dikirim ke database
                    val dataKos = hashMapOf(
                        "ownerUid" to currentUserUid,
                        "namaKos" to namaKos,
                        "hargaKos" to hargaKos,
                        "status" to "Tersedia"
                    )

                    // 4. Push data ke Firebase Realtime Database
                    databaseRef.child(currentUserUid).setValue(dataKos)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Berhasil mengiklankan kos: $namaKos!", Toast.LENGTH_LONG).show()
                            // Opsional: Bersihkan form setelah sukses disimpan
                            etNamaKos.text.clear()
                            etHargaKos.text.clear()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(requireContext(), "Gagal menyimpan ke database: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Sesi login berakhir, silakan login ulang.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Semua data form kamar wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        menuStatusBayar.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Status Bayar Penghuni", Toast.LENGTH_SHORT).show()
        }

        menuBroadcastChat.setOnClickListener {
            // 1. Instansiasi fragment chat pemilik yang sudah kamu buat di folder ui
            val fragmentTujuan = ChatPemilikFragment()

            // 2. Lakukan transaksi fragment untuk menumpuk halaman saat ini
            // CATATAN: Ganti 'R.id.fragment_container' dengan ID container yang ada di activity_main.xml kelompokmu
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentTujuan)
                .addToBackStack(null) // Fungsi agar ketika ditekan tombol back, kembali ke dashboard (tidak keluar aplikasi)
                .commit()
        }

        menuUlasanPenghuni.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Daftar Review & Bintang Kos", Toast.LENGTH_SHORT).show()
        }

        menuFiturLainnya.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Pengaturan Properti", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}