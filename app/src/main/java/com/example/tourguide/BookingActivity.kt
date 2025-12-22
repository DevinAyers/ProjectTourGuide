package com.example.tourguide

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class BookingActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var selectedImageUri: Uri? =null
    private var tourId : String?=""
    private var tourHarga: Int?=0
    private var tourJudul: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tourId=intent.getStringExtra("id")?:""
        tourHarga= intent.getIntExtra("harga",0)
        tourJudul = intent.getStringExtra("judul")?:""

        try {
            val config = hashMapOf(
                "cloud_name" to "dootst4tn",
                "upload_preset" to "preset_pembayaran",
                "secure" to true
            )
            MediaManager.init(this, config)

        }catch (e: Exception){
            Log.e("BookingActivity", " run config : ${e.message}")
        }



        val tvBookingJudul = findViewById<TextView>(R.id.tvBookingJudul)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etTelpon = findViewById<EditText>(R.id.etTelpon)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)
        val tvTotalHarga = findViewById<TextView>(R.id.tvTotalHarga)
        val imgBukti = findViewById<ImageView>(R.id.imgBukti)
        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitBooking)


        //Ambil data dari Detail
        val tour = intent.getParcelableExtra<TourData>("tour")
        val tanggal = intent.getStringExtra("tanggal") ?:""
        val jam = intent.getStringExtra("jam")?:""

        if (tour == null) {
            Toast.makeText(this, "Data tour tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvBookingJudul.text = "Booking ${tour.judul}"

        etJumlah.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val junlah = s.toString().toIntOrNull() ?:0
                val total = junlah * tour.harga
                tvTotalHarga.text = "Total Bayar : Rp $total"
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
        })

        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
            if (uri!=null){
                selectedImageUri = uri
                imgBukti.setImageURI(uri)

            }

        }
        btnUpload.setOnClickListener {
            imagePicker.launch("image/*")
        }


        btnSubmit.setOnClickListener {
            btnSubmit.isEnabled = false
            val nama = etNama.text.toString().trim()
            val telpon = etTelpon.text.toString().trim()
            val jumlah = etJumlah.text.toString().trim()

            val jumlahOrangInt = jumlah.toIntOrNull() ?:0

            val sisaKuota = tour.kuota - jumlahOrangInt

            if (sisaKuota < 0 ){
                Toast.makeText(this, "Kuota tidak mencukupi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (jumlahOrangInt < 0){
                Toast.makeText(this, "Jumlah orang harus lebih dari 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (nama.isEmpty() || telpon.isEmpty() || jumlah.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Tolong upload bukti pembayaran", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UploaadData(nama,telpon,jumlahOrangInt,tour,tanggal,jam)

        }

        val btnBack2 = findViewById<ImageView>(R.id.btnBack2)
        btnBack2.setOnClickListener {
            finish()
        }
    }

    private fun UploaadData(nama: String, telpon: String, jumlah: Int, tour: TourData, tanggal: String, jam: String){
        val bookingId = "BOOK-${System.currentTimeMillis()}"

        MediaManager.get().upload(selectedImageUri)
            .unsigned("preset_pembayaran")
            .option("public_id", "pembayaran/$bookingId")
            .callback(object : UploadCallback{
                override fun onStart(requestId: String?) {
                }

                override fun onProgress(
                    requestId: String?,
                    bytes: Long,
                    totalBytes: Long
                ) {
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val buktriUrl = resultData?.get("secure_url").toString()
                    Toast.makeText(this@BookingActivity, "Upload berhasil", Toast.LENGTH_SHORT).show()
                    UploadFirestore(nama, telpon, jumlah, tour, tanggal, jam, buktriUrl, bookingId)


                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@BookingActivity, "Gagal upload gambar", Toast.LENGTH_SHORT).show()
                    Log.e("BookingActivity", "gagal upload gambar ${error?.description}")
                }

                override fun onReschedule(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                }
            }).dispatch()
    }


    private fun UploadFirestore(nama: String, telpon: String, jumlah: Int, tour: TourData, tanggal: String, jam: String, buktiUrl: String, bookingId: String){

        val user  = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }
        val harga = tour.harga.toInt()
        val totalHarga = harga*jumlah
        val expiryTime = System.currentTimeMillis() + (30*60*1000) //30 mnt
        val bookingData = hashMapOf(
            "id" to bookingId,
            "userId" to user?.uid,
            "userEmail" to user?.email,
            "gambarUrl" to tour.gambarUrl,
            "judul" to tour.judul,
            "kota" to tour.kota,
            "tanggal" to tanggal,
            "jam" to jam,
            "nama" to nama,
            "telpon" to telpon,
            "jumlah" to jumlah,
            "harga" to harga,
            "totalHarga" to totalHarga,
            "paymentMethod" to "QRIS",
            "status" to "SUCCESS",
            "tourId" to tour.id,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp(),
            "expiryTimestamp" to expiryTime,
            "buktiPembayaranUrl" to buktiUrl

        )

        db.collection("booking").document(bookingId)
            .set(bookingData)
            .addOnSuccessListener {
                Toast.makeText(this, "Booking berhasil!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal booking", Toast.LENGTH_SHORT).show()
            }

        db.collection("tours").document(tour.id)
            .update("kuota", FieldValue.increment(-jumlah.toLong()))
            .addOnSuccessListener {
                Log.d("BookingActivity", "berhasil update kuota")
                finish()
            }
            .addOnFailureListener {
                Log.e("BookingActivity", "gagal update kuota", it)
            }

    }

}
