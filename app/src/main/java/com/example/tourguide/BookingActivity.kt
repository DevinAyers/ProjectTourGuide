package com.example.tourguide

import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.firestore.firestore

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

        val db = Firebase.firestore

        val btnBack2 = findViewById<ImageView>(R.id.btnBack2)
        btnBack2.setOnClickListener {
            finish()
        }

        val tvBookingJudul = findViewById<TextView>(R.id.tvBookingJudul)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etTelpon = findViewById<EditText>(R.id.etTelpon)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)

        val judul = intent.getStringExtra("judul")
        val tanggal = intent.getStringExtra("tanggal")
        val jam = intent.getStringExtra("jam")

        tvBookingJudul.text = "Booking $judul"

        val btnSubmit = findViewById<Button>(R.id.btnSubmitBooking)

        btnSubmit.setOnClickListener {


            val data = hashMapOf(
                "judul" to judul,
                "tanggal" to tanggal,
                "jam" to jam,
                "nama" to etNama.text.toString(),
                "telpon" to etTelpon.text.toString(),
                "jumlah" to etJumlah.text.toString()
            )
            db.collection("booking")
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Booking sukses!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}