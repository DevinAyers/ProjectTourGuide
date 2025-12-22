package com.example.tourguide

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryData(
    val judul: String,
    val nama: String,
    val telpon: String,
    val jumlah: Int=0,
    val totalHarga: Int=0,
    val status : String ="",
    val tanggal: String,
    val jam: String,
    val kota: String,
    val gambarUrl: String,
    val buktiUrl: String
): Parcelable
