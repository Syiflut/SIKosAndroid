package com.kelompok5.sikos.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kelompok5.sikos.R
import com.kelompok5.sikos.adapter.RiwayatAdapter
import com.kelompok5.sikos.model.Riwayat
import java.util.UUID

class RiwayatFragment : Fragment() {

    private lateinit var rvRiwayatPembayaran: RecyclerView
    private val listRiwayat = ArrayList<Riwayat>()
    private lateinit var riwayatAdapter: RiwayatAdapter

    private lateinit var btnPilihGambar: Button
    private lateinit var btnKirimBukti: Button
    private lateinit var tvStatusGambar: TextView
    private var imageUri: Uri? = null

    // Inisialisasi Firebase
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance()

    // Launcher untuk membuka galeri secara aman
    private val ambilGambarGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            tvStatusGambar.text = "Gambar berhasil dipilih!"
        } else {
            Toast.makeText(requireContext(), "Batal memilih gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat_penghuni, container, false)

        // Inisialisasi RecyclerView
        rvRiwayatPembayaran = view.findViewById(R.id.rvRiwayatPembayaran)
        rvRiwayatPembayaran.layoutManager = LinearLayoutManager(context)

        // Data dummy awal disesuaikan dengan nama parameter model baru
        val listDummyRiwayat = listOf(
            Riwayat(idTransaksi = "TX001", namaKos = "Kos Alinda", tanggalBayar = "15 Mar 2026", totalHarga = 1950000.0, durasiBulan = 3, statusPembayaran = "Lunas"),
            Riwayat(idTransaksi = "TX002", namaKos = "Kos Syifa", tanggalBayar = "10 Apr 2026", totalHarga = 800000.0, durasiBulan = 1, statusPembayaran = "Menunggu")
        )
        listRiwayat.clear()
        listRiwayat.addAll(listDummyRiwayat)

        riwayatAdapter = RiwayatAdapter(listRiwayat)
        rvRiwayatPembayaran.adapter = riwayatAdapter

        // Inisialisasi Komponen UI
        btnPilihGambar = view.findViewById(R.id.btnPilihGambar)
        btnKirimBukti = view.findViewById(R.id.btnKirimBukti)
        tvStatusGambar = view.findViewById(R.id.tvStatusGambar)

        // Klik tombol pilih gambar dari galeri
        btnPilihGambar.setOnClickListener {
            ambilGambarGallery.launch("image/*")
        }

        // Klik tombol kirim untuk proses upload ke Firebase
        btnKirimBukti.setOnClickListener {
            if (imageUri != null) {
                uploadBuktiKeFirebase(imageUri!!)
            } else {
                Toast.makeText(requireContext(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // Fungsi untuk memproses upload berkas gambar
    private fun uploadBuktiKeFirebase(uri: Uri) {
        btnKirimBukti.isEnabled = false
        tvStatusGambar.text = "Sedang mengunggah bukti..."

        // Menentukan nama file acak unik di Firebase Storage folder 'bukti_pembayaran'
        val fileName = "bukti_" + UUID.randomUUID().toString() + ".jpg"
        val storageRef = storage.reference.child("bukti_pembayaran/$fileName")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Jika sukses upload gambar, ambil link URL download-nya
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val urlGambar = downloadUri.toString()

                    // Setelah dapat URL gambar, simpan data transaksinya ke Realtime Database
                    simpanDataKeDatabase(urlGambar)
                }
            }
            .addOnFailureListener { e ->
                btnKirimBukti.isEnabled = true
                tvStatusGambar.text = "Gagal mengunggah gambar."
                Toast.makeText(requireContext(), "Error Storage: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    // Fungsi untuk menyimpan teks data ke Realtime Database
    private fun simpanDataKeDatabase(urlGambar: String) {
        val databaseRef = database.reference.child("pembayaran")

        // Membuat ID transaksi baru atau gunakan ID yang ada
        val idTransaksiBaru = databaseRef.push().key ?: "TX003"

        // Membuat objek model data baru yang disesuaikan dengan variabel Riwayat.kt terbarumu
        val dataPembayaran = Riwayat(
            idTransaksi = idTransaksiBaru,
            namaKos = "Kos Syifa",
            tanggalBayar = "13 Jul 2026",
            totalHarga = 800000.0,
            durasiBulan = 1,
            statusPembayaran = "Menunggu",
            buktiBayarUrl = urlGambar
        )

        databaseRef.child(idTransaksiBaru).setValue(dataPembayaran)
            .addOnSuccessListener {
                tvStatusGambar.text = "Bukti berhasil dikirim ke Admin!"
                btnKirimBukti.isEnabled = true
                imageUri = null
                Toast.makeText(requireContext(), "Pembayaran Sukses Dikirim!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                btnKirimBukti.isEnabled = true
                tvStatusGambar.text = "Gagal menyimpan data transaksi."
                Toast.makeText(requireContext(), "Error Database: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}