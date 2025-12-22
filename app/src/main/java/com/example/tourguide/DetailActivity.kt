package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvJudul = findViewById<TextView>(R.id.tvJudul)
        val tvDeskripsi = findViewById<TextView>(R.id.tvDeskripsi)
        val tvKuota = findViewById<TextView>(R.id.tvKuota)
        val spinnerTanggal = findViewById<Spinner>(R.id.spinnerTanggal)
        val spinnerJam = findViewById<Spinner>(R.id.spinnerJam)
        val imgDetail = findViewById<ImageView>(R.id.imgDetail)
        val btnBooking = findViewById<Button>(R.id.btnBooking)
        val db = FirebaseFirestore.getInstance()

        val tour = intent.getParcelableExtra<TourData>("tour")

        if (tour == null) {
            finish()
            return
        }

        db.collection("tours").document(tour.id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    finish()
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val kuotaTerbaru = snapshot.getLong("kuota")?.toInt()
                    tvKuota.text = "Kuota: $kuotaTerbaru"
                    tour.kuota = kuotaTerbaru?:0
                }
            }//supaya update counter kuota nya jalan

        tvJudul.text = tour.judul
        tvDeskripsi.text = tour.deskripsi
        tvKuota.text = "Kuota: ${tour.kuota}"

        Glide.with(this)
            .load(tour.gambarUrl)
            .into(imgDetail)

        spinnerTanggal.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tour.tanggalTersedia
        )

        spinnerJam.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tour.jamTersedia
        )

        btnBooking.setOnClickListener {

            val auth = Firebase.auth

            val currUser = auth.currentUser
            Log.e("DetailActivity", "Current user: $currUser")

            if (currUser!=null){
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra("tour", tour)
                intent.putExtra("tanggal", spinnerTanggal.selectedItem.toString())
                intent.putExtra("jam", spinnerJam.selectedItem.toString())
                startActivity(intent)
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }



        }

        val btnBack = findViewById<ImageView>(R.id.btnBack3)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
