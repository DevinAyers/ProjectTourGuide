package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var rvTours: RecyclerView
    private lateinit var adapter: TourAdapter
    private val list = ArrayList<TourData>()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        rvTours = findViewById(R.id.rvTours)
        rvTours.layoutManager = LinearLayoutManager(this)

        adapter = TourAdapter(list) { tour ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("tour", tour)
            startActivity(intent)
        }

        rvTours.adapter = adapter

        val etSearch = findViewById<EditText>(R.id.etSearch)

        etSearch.addTextChangedListener{ text->
            val query = text.toString()
            adapter.query(query)
        }

        loadToursFromFirebase()

        findViewById<LinearLayout>(R.id.btnFooterHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun loadToursFromFirebase() {
        db.collection("tours")
            .get()
            .addOnSuccessListener { result ->
                list.clear()

                for (doc in result) {
                    val tour = doc.toObject(TourData::class.java)
                    list.add(tour)
                }

                adapter.updateData(list)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal ambil data tour", Toast.LENGTH_SHORT).show()
            }
    }
}
