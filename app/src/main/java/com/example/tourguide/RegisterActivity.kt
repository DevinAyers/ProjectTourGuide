package com.example.tourguide


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.length >=6){//defailt fireabse 6 karakter password
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) { task ->  //async tunggu operasi db slsai, (this) disini tujuannya untuk mengunci listener dengan aktivity ini
                        if (task.isSuccessful){
                            Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this, "Register Fail", Toast.LENGTH_SHORT).show()
                        }


                    }

            }else{
                Toast.makeText(this, "Invalid Register Data", Toast.LENGTH_SHORT).show()
            }

        }




    }


}