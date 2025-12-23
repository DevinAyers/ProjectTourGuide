package com.example.tourguide

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class TourData(

    val id: String = "",
    val judul: String ="",
    val deskripsi: String ="",
    val kota: String ="",
    var kuota: Int=0,
    val gambarUrl: String="",
    val harga: Int=0,
    val jamTersedia : List<String> = listOf(),
    val tanggalTersedia : List<String> = listOf()
) : Parcelable
