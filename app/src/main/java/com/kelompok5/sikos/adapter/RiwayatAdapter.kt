package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.model.Riwayat

class RiwayatAdapter(private val listRiwayat: List<Riwayat>) :
    RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKos: TextView = view.findViewById(R.id.tvRiwayatNamaKos)
        val tvTanggal: TextView = view.findViewById(R.id.tvRiwayatTanggal)
        val tvTotalHarga: TextView = view.findViewById(R.id.tvRiwayatTotalHarga)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusPembayaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_pembayaran, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = listRiwayat[position]
        holder.tvNamaKos.text = riwayat.namaKos
        holder.tvTanggal.text = "Pembayaran: ${riwayat.tanggalBayar}"
        holder.tvTotalHarga.text = "Total: Rp ${riwayat.totalHarga.toInt()} (${riwayat.durasiBulan} Bulan)"
        holder.tvStatus.text = riwayat.statusPembayaran

        // Mengubah warna background tag status sesuai kondisinya biar kontras imut
        if (riwayat.statusPembayaran == "Lunas") {
            holder.tvStatus.setBackgroundResource(R.color.pastel_blue_dark) // Biru kalau lunas
        } else {
            holder.tvStatus.setBackgroundResource(R.color.pastel_pink) // Pink kalau menunggu/proses
        }
    }

    override fun getItemCount(): Int = listRiwayat.size
}