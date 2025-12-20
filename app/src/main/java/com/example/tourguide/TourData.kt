package com.example.tourguide

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TourData(
    val id: String = "",
    val judul: String ="",
    val deskripsi: String ="",
    val kota: String ="",
    val kuota: Int=0,
    val gambarUrl: String="",
    val jamTersedia : List<String> = listOf(),
    val tanggalTersedia : List<String> = listOf()
) : Parcelable
