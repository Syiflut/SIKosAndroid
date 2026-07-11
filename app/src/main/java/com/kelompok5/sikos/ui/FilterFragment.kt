package com.kelompok5.sikos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.kelompok5.sikos.R
import java.text.NumberFormat
import java.util.Locale

class FilterFragment(
    private val onFilterApplied: (alamat: String, tipe: String, rating: Float, minHarga: Double, maxHarga: Double) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_filter_kos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sliderHarga = view.findViewById<RangeSlider>(R.id.sliderHarga)
        val spinnerAlamat = view.findViewById<Spinner>(R.id.spinnerAlamat)
        val tvRentangHarga = view.findViewById<TextView>(R.id.tvRentangHarga)
        val rgTipeKos = view.findViewById<RadioGroup>(R.id.rgTipeKos)
        val ratingBarFilter = view.findViewById<RatingBar>(R.id.ratingBarFilter)
        val btnTerapkanFilter = view.findViewById<Button>(R.id.btnTerapkanFilter)

        sliderHarga.setValues(500000f, 2000000f)

        val listAlamat = arrayOf(
            "Semua Lokasi",
            "Bandung",
            "Garut",
            "Sumedang",
            "Dekat Kampus"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listAlamat)
        spinnerAlamat.adapter = adapter

        sliderHarga.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val minHarga = values[0].toInt()
            val maxHarga = values[1].toInt()

            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            formatRupiah.maximumFractionDigits = 0

            val hasilTeksHarga = "${formatRupiah.format(minHarga)} - ${formatRupiah.format(maxHarga)}"
            tvRentangHarga.text = hasilTeksHarga
        }

        btnTerapkanFilter.setOnClickListener {
            val alamatTerpilih = spinnerAlamat.selectedItem.toString()

            val idTipeTerpilih = rgTipeKos.checkedRadioButtonId
            val tipeTerpilih = when (idTipeTerpilih) {
                R.id.rbPutra -> "Putra"
                R.id.rbPutri -> "Putri"
                else -> "Campur"
            }

            val ratingTerpilih = ratingBarFilter.rating
            val minHarga = sliderHarga.values[0].toDouble()
            val maxHarga = sliderHarga.values[1].toDouble()

            onFilterApplied(alamatTerpilih, tipeTerpilih, ratingTerpilih, minHarga, maxHarga)
            dismiss()
        }
    }
}