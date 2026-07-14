package com.kelompok5.sikos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.sikos.R
import com.kelompok5.sikos.ui.PaymentModel

class PaymentAdapter(
    private val items: List<PaymentModel>,
    private val onConfirmClick: (String) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNamaKos: TextView = v.findViewById(R.id.tvNamaKosBayar)
        val tvDetail: TextView = v.findViewById(R.id.tvDetailBayar)
        val tvStatus: TextView = v.findViewById(R.id.tvStatusBayar)
        val btnKonfirmasi: Button = v.findViewById(R.id.btnKonfirmasiBayar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_konfirmasi_bayar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]

        holder.tvNamaKos.text = "Pengajuan Sewa Baru"
        holder.tvDetail.text = "Mulai Masuk: ${data.tanggalMasuk}\nDurasi Sewa: ${data.durasi} Bulan"
        holder.tvStatus.text = "Status: ${data.status}"

        holder.btnKonfirmasi.setOnClickListener {
            onConfirmClick(data.bookingId)
        }
    }

    override fun getItemCount(): Int = items.size
}