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

class LoginActivity : AppCompatActivity() {
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        loginButton = findViewById(R.id.loginButton)
        emailField = findViewById(R.id.emailInput)
        passwordField = findViewById(R.id.passwordInput)

        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}