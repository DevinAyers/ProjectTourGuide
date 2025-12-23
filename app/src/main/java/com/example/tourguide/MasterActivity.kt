package com.example.tourguide

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MasterActivity: AppCompatActivity()  {

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_master)

        val btnMasterSubmit = findViewById<Button>(R.id.btnMasterSubmit)

        btnMasterSubmit.setOnClickListener {
            submitMaster()
        }

        val btnBack = findViewById<ImageView>(R.id.btnCancelInputDataMasterTour)
        btnBack.setOnClickListener {
            finish()
        }


    }

    private fun submitMaster(){
        val user  = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }
        val judul = findViewById<EditText>(R.id.etMasterJudul)
        val kota = findViewById<EditText>(R.id.etMasterKota)
        val kuota = findViewById<EditText>(R.id.etMasterKuota)
        val harga = findViewById<EditText>(R.id.etMasterHarga)
        val deskripsi = findViewById<EditText>(R.id.etMasterDeskripsi)
        val urlGambar = findViewById<EditText>(R.id.etMasterUrlGambar)
        val tanggal = findViewById<EditText>(R.id.etMasterTanggal)
        val jam = findViewById<EditText>(R.id.etMasterJam)

        val listTanggal = tanggal.text.toString().trim().split(",")
        val listJam = jam.text.toString().trim().split(",")

        if (judul.text.toString().trim().isEmpty() ||
            kota.text.toString().trim().isEmpty() ||
            kuota.text.toString().trim().isEmpty() ||
            harga.text.toString().trim().isEmpty() ||
            deskripsi.text.toString().trim().isEmpty() ||
            urlGambar.text.toString().trim().isEmpty() ||
            listTanggal.isEmpty() ||
            listJam.isEmpty()
        ) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return

        }

        val tourId = "TOUR-${System.currentTimeMillis()}"

        val masterTourData = hashMapOf(
            "id" to tourId,
            "deskripsi" to deskripsi.text.toString().trim(),
            "gambarUrl" to urlGambar.text.toString().trim(),
            "harga" to harga.text.toString().toInt(),
            "jamTersedia" to listJam,
            "judul" to judul.text.toString().trim(),
            "kota" to kota.text.toString().trim(),
            "kuota" to kuota.text.toString().toInt(),
            "tanggalTersedia" to listTanggal
        )

        db.collection("tours").document(tourId)
            .set(masterTourData)
            .addOnSuccessListener {
                Toast.makeText(this, "Master berhasil!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.e("MasterActivity", "gagal master", it)
                Toast.makeText(this, "Gagal master", Toast.LENGTH_SHORT).show()
            }







    }
}



