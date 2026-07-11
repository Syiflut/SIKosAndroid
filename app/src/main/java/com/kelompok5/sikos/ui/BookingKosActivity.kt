package com.kelompok5.sikos.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelompok5.sikos.R

class BookingKosActivity : AppCompatActivity() {

    private lateinit var etTanggalMasuk: EditText
    private lateinit var etDurasiSewa: EditText
    private lateinit var btnKirimBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menyambungkan logika dengan layout activity_booking_kos.xml yang imut tadi
        setContentView(R.layout.activity_booking_kos)

        // Inisialisasi komponen input sesuai id di XML
        etTanggalMasuk = findViewById(R.id.etTanggalMasuk)
        etDurasiSewa = findViewById(R.id.etDurasiSewa)
        btnKirimBooking = findViewById(R.id.btnKirimBooking)

        // Aksi ketika tombol Kirim Pengajuan Sewa diklik
        btnKirimBooking.setOnClickListener {
            val tanggal = etTanggalMasuk.text.toString().trim()
            val durasi = etDurasiSewa.text.toString().trim()

            // Validasi sederhana: pastikan data tidak kosong sebelum diproses Firebase
            if (tanggal.isEmpty() || durasi.isEmpty()) {
                Toast.makeText(this, "Data wajib diisi, Bibu!", Toast.LENGTH_SHORT).show()
            } else {
                // Di sini nanti tempat Anggota 3 menyambungkan ke Firebase database kelompokmu
                Toast.makeText(this, "Pengajuan sewa berhasil dikirim!", Toast.LENGTH_LONG).show()
                finish() // Menutup halaman dan kembali ke dashboard
            }
        }
    }
}