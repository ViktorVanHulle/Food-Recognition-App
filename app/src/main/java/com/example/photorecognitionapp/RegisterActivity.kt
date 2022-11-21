package com.example.photorecognitionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*
        name = findViewById(R.id.registerNameInput)
        email = findViewById(R.id.registerEmailInput)
        password = findViewById(R.id.registerPasswordInput)
        registerButton = findViewById(R.id.registerButton)
        auth = Firebase.auth

         */

        registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val intent = Intent(this, LoginActivity::class.java)
                    Toast.makeText(baseContext, "Successfully registered", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Failed to register", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}