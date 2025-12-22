package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ProfileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val auth = Firebase.auth
        val tvEmail = findViewById<TextView>(R.id.tvUserEmail)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val emailUser = auth.currentUser?.email?: "Silakan Login"
        tvEmail.text = emailUser

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this,"Logout berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}