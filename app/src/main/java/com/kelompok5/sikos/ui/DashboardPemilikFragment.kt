package com.kelompok5.sikos.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kelompok5.sikos.R
import java.util.UUID

class DashboardPemilikFragment : Fragment() {

    private lateinit var etNamaKos: EditText
    private lateinit var etHargaKos: EditText
    private lateinit var etAlamatKos: EditText
    private lateinit var tvNamaPemilik: TextView

    private lateinit var btnPilihFoto: Button
    private lateinit var btnSimpanKos: Button

    private lateinit var menuStatusBayar: View
    private lateinit var menuBroadcastChat: View
    private lateinit var menuUlasanPenghuni: View

    private var fotoUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    private val launcherGaleri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fotoUri = result.data?.data
            if (fotoUri != null) {
                btnPilihFoto.text = "Foto Terpilih! ✅"
                Toast.makeText(requireContext(), "Foto berhasil dimuat.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_pemilik, container, false)

        etNamaKos = view.findViewById(R.id.etNamaKos)
        etHargaKos = view.findViewById(R.id.etHargaKos)
        etAlamatKos = view.findViewById(R.id.etAlamatKos)
        tvNamaPemilik = view.findViewById(R.id.tvNamaPemilik)

        btnPilihFoto = view.findViewById(R.id.btnPilihFoto)
        btnSimpanKos = view.findViewById(R.id.btnSimpanKos)

        menuStatusBayar = view.findViewById(R.id.menuStatusBayar)
        menuBroadcastChat = view.findViewById(R.id.menuBroadcastChat)
        menuUlasanPenghuni = view.findViewById(R.id.menuUlasanPenghuni) ?: view.findViewById(android.R.id.content)

        val namaPemilik = arguments?.getString("KEY_NAMA") ?: "Owner"
        tvNamaPemilik.text = "Halo, $namaPemilik! (Owner)"

        btnPilihFoto.setOnClickListener {
            bukaGaleri()
        }

        btnSimpanKos.setOnClickListener {
            prosesUnggahDanSimpanKos()
        }

        menuStatusBayar.setOnClickListener {
            val fragmentTujuan = StatusBayarPemilikFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentTujuan)
                .addToBackStack(null)
                .commit()
        }

        menuBroadcastChat.setOnClickListener {
            val fragmentTujuan = ChatPemilikFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentTujuan)
                .addToBackStack(null)
                .commit()
        }

        menuUlasanPenghuni.setOnClickListener {
            val fragmentTujuan = LihatUlasanFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentTujuan)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun bukaGaleri() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launcherGaleri.launch(intent)
    }

    private fun prosesUnggahDanSimpanKos() {
        val namaKos = etNamaKos.text.toString().trim()
        val hargaKos = etHargaKos.text.toString().trim()
        val alamatKos = etAlamatKos.text.toString().trim()

        if (namaKos.isEmpty() || hargaKos.isEmpty() || alamatKos.isEmpty()) {
            Toast.makeText(requireContext(), "Semua data form kamar wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        btnSimpanKos.text = "Menyimpan..."
        btnSimpanKos.isEnabled = false

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            if (fotoUri != null) {
                val pathFileFoto = "foto_kos/${UUID.randomUUID()}.jpg"
                val refFoto = storageRef.child(pathFileFoto)

                refFoto.putFile(fotoUri!!)
                    .addOnSuccessListener {
                        refFoto.downloadUrl.addOnSuccessListener { uri ->
                            val urlFotoDownload = uri.toString()
                            simpanKatalogKeDatabase(currentUserUid, namaKos, hargaKos, alamatKos, urlFotoDownload)
                        }
                    }
                    .addOnFailureListener { error ->
                        btnSimpanKos.text = "Lengkapi & Simpan Iklan"
                        btnSimpanKos.isEnabled = true
                        Toast.makeText(requireContext(), "Gagal unggah gambar: ${error.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                simpanKatalogKeDatabase(currentUserUid, namaKos, hargaKos, alamatKos, "")
            }
        } else {
            btnSimpanKos.text = "Lengkapi & Simpan Iklan"
            btnSimpanKos.isEnabled = true
            Toast.makeText(requireContext(), "Sesi login berakhir, silakan login ulang.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun simpanKatalogKeDatabase(ownerUid: String, namaKos: String, hargaKos: String, lokasiKos: String, urlFoto: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("kos_properties")
        val idKosBaru = databaseRef.push().key ?: UUID.randomUUID().toString()

        val dataKos = hashMapOf(
            "idKos" to idKosBaru,
            "ownerUid" to ownerUid,
            "namaKos" to namaKos,
            "hargaKos" to hargaKos,
            "lokasi" to lokasiKos,
            "urlFoto" to urlFoto,
            "status" to "Tersedia"
        )

        databaseRef.child(idKosBaru).setValue(dataKos)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Berhasil mengiklankan kos: $namaKos!", Toast.LENGTH_LONG).show()

                etNamaKos.text.clear()
                etHargaKos.text.clear()
                etAlamatKos.text.clear()
                fotoUri = null
                btnPilihFoto.text = "Pilih & Unggah Foto Properti"

                btnSimpanKos.text = "Lengkapi & Simpan Iklan"
                btnSimpanKos.isEnabled = true
            }
            .addOnFailureListener { error ->
                btnSimpanKos.text = "Lengkapi & Simpan Iklan"
                btnSimpanKos.isEnabled = true
                Toast.makeText(requireContext(), "Gagal menyimpan katalog: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}