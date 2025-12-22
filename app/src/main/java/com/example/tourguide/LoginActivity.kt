package com.example.tourguide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity: AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val currUser = auth.currentUser
        if (currUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val etEmail = findViewById<EditText>(R.id.etEmailLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToRegister= findViewById<TextView>(R.id.tvGoToRegister)
        btnLogin.setOnClickListener {

            val email= etEmail.text.toString()
            val password= etPassword.text.toString().trim()
            Log.e("Login", "Email: $email, Password: $password")

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        val intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()
                        Log.e("Login", "Login failed: ${task.exception}")
                    }

//                    val uid =auth.currentUser?.uid
//                    val db  = FirebaseFirestore.getInstance()

//                    if (uid!=null){
//                        db.collection("users").document(uid).get()
//                            .addOnSuccessListener { data->
//                                val role = data.getString("role")
//                                if (role=="admin"){
//                                    val intent = Intent(this, AdminActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }else{
//                                    val intent = Intent(this, HomeActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }
//
//
//                            }
//                            .addOnFailureListener {
//                                val intent = Intent(this, HomeActivity::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                    }

                }

        }

        tvGoToRegister.setOnClickListener {
            val inyent = Intent(this, RegisterActivity::class.java)
            startActivity(inyent)
        }



    }


}