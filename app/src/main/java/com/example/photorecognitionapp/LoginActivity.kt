package com.example.photorecognitionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"
        auth = Firebase.auth
        emailLogin = findViewById(R.id.editTextEmailAddress)
        passwordLogin = findViewById(R.id.editTextPassword)
    }

    fun login(view: View){
        val email=emailLogin.text.toString()
        val password=passwordLogin.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("sign in", "signInWithEmail:success")
                    val user = auth.currentUser
                    val userid = user?.uid

                    val intent = Intent(this,DashboardActivity::class.java)
                    intent.putExtra("userId", userid)

                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("sign in", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}