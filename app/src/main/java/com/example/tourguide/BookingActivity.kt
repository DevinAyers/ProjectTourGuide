package com.example.tourguide

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class BookingActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = FirebaseFirestore.getInstance()

        val tvBookingJudul = findViewById<TextView>(R.id.tvBookingJudul)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etTelpon = findViewById<EditText>(R.id.etTelpon)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitBooking)

        //Ambil data dari Detail
        val tour = intent.getParcelableExtra<TourData>("tour")
        val tanggal = intent.getStringExtra("tanggal")
        val jam = intent.getStringExtra("jam")

        if (tour == null) {
            Toast.makeText(this, "Data tour tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvBookingJudul.text = "Booking ${tour.judul}"


        btnSubmit.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val telpon = etTelpon.text.toString().trim()
            val jumlah = etJumlah.text.toString().trim()

            val jumlahOrangInt = etJumlah.text.toString().toInt()
            val sisaKuota = tour.kuota - jumlahOrangInt
            if (sisaKuota < 0 ){
                Toast.makeText(this, "Kuota tidak mencukupi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nama.isEmpty() || telpon.isEmpty() || jumlah.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user  = Firebase.auth.currentUser
            if (user == null) {
                Toast.makeText(this, "Silakan login ulang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val bookingData = hashMapOf(
                "userId" to user?.uid,
                "userEmail" to user?.email,
                "gambarUrl" to tour.gambarUrl,
                "judul" to tour.judul,
                "kota" to tour.kota,
                "tanggal" to tanggal,
                "jam" to jam,
                "nama" to nama,
                "telpon" to telpon,
                "jumlah" to jumlah
            )

            db.collection("booking")
                .add(bookingData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Booking berhasil!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal booking", Toast.LENGTH_SHORT).show()
                }

            db.collection("tours").document(tour.id)
                .update("kuota",sisaKuota)
                .addOnSuccessListener {
                    Log.d("BookingActivity", "berhasil update kuota")
                    finish()
                }
                .addOnFailureListener {
                    Log.e("BookingActivity", "gagal update kuota", it)
                }
        }

        val btnBack2 = findViewById<ImageView>(R.id.btnBack2)
        btnBack2.setOnClickListener {
            finish()
        }
    }
}
