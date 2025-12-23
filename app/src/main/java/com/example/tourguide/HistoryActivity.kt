package com.example.tourguide

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class HistoryActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var rv: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private val list = ArrayList<HistoryData>()

    private lateinit var zoomLayout: FrameLayout
    private lateinit var imgZoomed: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        zoomLayout = findViewById(R.id.zoomLayout)
        imgZoomed = findViewById(R.id.imgZoomed)
        val btnCloseZoom = findViewById<ImageView>(R.id.btnCloseZoom)
        rv = findViewById(R.id.rvHistory)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(list){url->
            zoomImage(url)
        }
        rv.adapter = adapter

        loadHistory()

        val btnBack3 = findViewById<ImageView>(R.id.btnBack3)
        btnBack3.setOnClickListener { finish() }

        val btnFooterHistory = findViewById<LinearLayout>(R.id.btnFooterHistory)
        val btnFooterHome = findViewById<LinearLayout>(R.id.btnFooterHome)

        btnFooterHistory.setOnClickListener {

        }

        btnFooterHome.setOnClickListener {
            finish()
        }

        btnCloseZoom.setOnClickListener {
            zoomLayout.visibility = View.GONE
        }

    }

    private fun zoomImage(url :String){
        zoomLayout.visibility = View.VISIBLE
        Glide.with(this).load(url).placeholder(R.drawable.banner2).
        error(R.drawable.banner2).into(imgZoomed)

    }

    private fun loadHistory() {
        val auth = Firebase.auth

        val currUser = auth.currentUser

        if (currUser==null){
            Toast.makeText(this,"Silakan login", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        db.collection("booking")
            .whereEqualTo("userId",currUser.uid)
            .get()
            .addOnSuccessListener { result ->
                list.clear()
                for (doc in result) {
                    val data = HistoryData(
                        judul = doc.getString("judul") ?: "",
                        nama = doc.getString("nama") ?: "",
                        telpon = doc.getString("telpon") ?: "",
                        jumlah = doc.get("jumlah").toString()?.toIntOrNull() ?: 0,
                        harga= doc.getLong("harga")?.toInt() ?: 0,
                        totalHarga = doc.getLong("totalHarga")?.toInt() ?: 0,
                        status = doc.getString("status") ?: "",
                        tanggal = doc.getString("tanggal") ?: "",
                        jam = doc.getString("jam") ?: "",
                        kota = doc.getString("kota") ?:"",
                        gambarUrl = doc.getString("gambarUrl") ?: "",
                        buktiUrl = doc.getString("buktiPembayaranUrl") ?:""
                    )
                    list.add(data)
                }
                if (list.isEmpty){
                    Toast.makeText(this,"Belum ada riwayat pemesanan", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {e->
                Log.e("HistoryActivity","gagal ambil data error ${e.message}")
            }

    }
}