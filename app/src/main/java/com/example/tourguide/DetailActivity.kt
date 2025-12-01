package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        // DATA dari HomeActivity
        val judul = intent.getStringExtra("judul")
        val kota = intent.getStringExtra("kota")
        val deskripsi = intent.getStringExtra("deskripsi")
        val kuota = intent.getIntExtra("kuota", 0)
        val gambar = intent.getIntExtra("gambar", 0)


        tvJudul.text = judul
        tvDeskripsi.text = deskripsi
        tvKuota.text = "Kuota: $kuota"
        imgDetail.setImageResource(gambar)


        val jamList = listOf("08.00 - 16.00", "08.00 - 20.00")
        spinnerJam.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jamList)

        val tglList = listOf("1 Des 2025", "2 Des 2025", "3 Des 2025", "4 Des 2025", "5 Des 2025")
        spinnerTanggal.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tglList)


        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("judul", judul)
            intent.putExtra("kota", kota)
            intent.putExtra("tanggal", spinnerTanggal.selectedItem.toString())
            intent.putExtra("jam", spinnerJam.selectedItem.toString())
            startActivity(intent)
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

    }
}