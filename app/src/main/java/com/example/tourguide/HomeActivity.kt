package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val container = findViewById<LinearLayout>(R.id.containerTours)


        fun setClick(index: Int, judul: String, des: String, img: Int) {
            val item = container.getChildAt(index) as LinearLayout
            item.setOnClickListener {
                val i = Intent(this, DetailActivity::class.java)
                i.putExtra("judul", judul)
                i.putExtra("deskripsi", des)
                i.putExtra("kuota", 10)
                i.putExtra("gambar", img)
                startActivity(i)
            }
        }

        setClick(0, "Jakarta", "Tour Guide Di Kota Jakarta", R.drawable.jakarta1)
        setClick(1, "Malang", "Tour Guide Di Kota Malang.", R.drawable.malang1)
        setClick(2, "Batu", "Tour Guide Di Kota Batu", R.drawable.batu1)
        setClick(3, "Bali", "Tour Guide Di Kota Bali", R.drawable.bali1)
        setClick(4, "Yogyakarta", "Tour Guide Di Kota Yogyakarta", R.drawable.yogyakarta1)
        setClick(5, "Surabaya", "Tour Guide Di Kota Surabaya", R.drawable.surabaya1)

//        val btnHistory = findViewById<Button>(R.id.btnHistory)
//        btnHistory.setOnClickListener {
//            startActivity(Intent(this, HistoryActivity::class.java))
//        }

        val btnFooterHome = findViewById<LinearLayout>(R.id.btnFooterHome)
        val btnFooterHistory = findViewById<LinearLayout>(R.id.btnFooterHistory)

        btnFooterHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        btnFooterHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

    }
}