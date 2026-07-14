package com.kelompok5.sikos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class BookingKosActivity : AppCompatActivity() {

    private lateinit var etTanggalMasuk: EditText
    private lateinit var etDurasiSewa: EditText
    private lateinit var btnPilihBuktiBayar: Button
    private lateinit var btnKirimPengajuan: Button

    private var uriBuktiBayar: Uri? = null

    private val launcherGaleri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedUri = result.data?.data
            if (selectedUri != null) {
                uriBuktiBayar = selectedUri
                btnPilihBuktiBayar.text = "Bukti Bayar Terpilih! ✅"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_kos)

        etTanggalMasuk = findViewById(R.id.etTanggalMasuk)
        etDurasiSewa = findViewById(R.id.etDurasiSewa)
        btnPilihBuktiBayar = findViewById(R.id.btnPilihBuktiBayar)
        btnKirimPengajuan = findViewById(R.id.btnKirimPengajuan)

        btnPilihBuktiBayar.setOnClickListener {
            bukaGaleri()
        }

        btnKirimPengajuan.setOnClickListener {
            prosesSimpanSewaDanBayar()
        }
    }

    private fun bukaGaleri() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launcherGaleri.launch(intent)
    }

    private fun prosesSimpanSewaDanBayar() {
        val tglMasuk = etTanggalMasuk.text.toString().trim()
        val durasi = etDurasiSewa.text.toString().trim()

        // Validasi: Bukti bayar dihapus dari syarat wajib
        if (tglMasuk.isEmpty() || durasi.isEmpty()) {
            Toast.makeText(this, "Mohon isi tanggal masuk dan durasi sewa!", Toast.LENGTH_SHORT).show()
            return
        }

        btnKirimPengajuan.text = "Mengirim Pengajuan..."
        btnKirimPengajuan.isEnabled = false

        val uidUser = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val bookingId = FirebaseDatabase.getInstance().getReference("bookings").push().key ?: UUID.randomUUID().toString()

        // Jika user memilih bukti bayar, upload ke Storage terlebih dahulu
        if (uriBuktiBayar != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val refBayar = storageRef.child("bukti_bayar/$bookingId.jpg")

            refBayar.putFile(uriBuktiBayar!!).addOnSuccessListener {
                refBayar.downloadUrl.addOnSuccessListener { urlBayar ->
                    simpanKeDatabase(bookingId, uidUser, tglMasuk, durasi, urlBayar.toString())
                }
            }.addOnFailureListener {
                resetTombol()
                Toast.makeText(this, "Gagal mengunggah gambar bukti pembayaran.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Jika dikosongkan, langsung simpan ke Realtime Database dengan url ""
            simpanKeDatabase(bookingId, uidUser, tglMasuk, durasi, "")
        }
    }

    private fun simpanKeDatabase(bookingId: String, uidUser: String, tglMasuk: String, durasi: String, urlBayar: String) {
        val dataSewa = hashMapOf(
            "bookingId" to bookingId,
            "penghuniId" to uidUser,
            "tanggalMasuk" to tglMasuk,
            "durasi" to durasi,
            "urlBuktiBayar" to urlBayar,
            "status" to "Menunggu Konfirmasi Owner"
        )

        FirebaseDatabase.getInstance().getReference("bookings").child(bookingId)
            .setValue(dataSewa)
            .addOnSuccessListener {
                Toast.makeText(this@BookingKosActivity, "Pengajuan sewa berhasil terkirim!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                resetTombol()
                Toast.makeText(this@BookingKosActivity, "Gagal mengirim data ke database.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resetTombol() {
        btnKirimPengajuan.text = "Kirim Pengajuan Sewa & Bayar"
        btnKirimPengajuan.isEnabled = true
    }
}