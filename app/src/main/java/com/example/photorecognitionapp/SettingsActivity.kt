package com.example.photorecognitionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.photorecognitionapp.firestore.CloudData
import com.example.photorecognitionapp.firestore.MealItem
import com.example.photorecognitionapp.firestore.UserSettings
import com.example.photorecognitionapp.firestore.getDate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity()  {
    lateinit var userId: String
    lateinit var cloudData: CloudData
    private val db = Firebase.firestore
    lateinit var amount_calories: EditText
    lateinit var amount_protein: EditText
    lateinit var amount_fats: EditText
    lateinit var amount_carbs: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        userId = intent.getStringExtra("userId").toString()
        cloudData = intent.getParcelableExtra<CloudData>("cloudData")!!

        amount_calories = findViewById(R.id.setGoalCalories)
        amount_protein = findViewById(R.id.setGoalProt)
        amount_fats = findViewById(R.id.setGoalFat)
        amount_carbs = findViewById(R.id.setGoalCarbs)

        //listen if button is clicked
        clickListener()
    }

    private fun clickListener(){

        // Initialize button amd userSettings object.
        val btn_setNutrients = findViewById<Button>(R.id.btn_setNutrients)
        val userSettings = UserSettings()

        // Get user settings and add data to userSettings object, else set default values.
        db.collection(userId).document("userSettings").get().addOnSuccessListener { document ->
            if(document.exists()) {
                userSettings.caloriesGoal = document.get("caloriesGoal").toString().toInt()
                userSettings.fatGoal = document.get("fatGoal").toString().toInt()
                userSettings.carbsGoal = document.get("carbsGoal").toString().toInt()
                userSettings.proteinGoal = document.get("proteinGoal").toString().toInt()
            } else {
                userSettings.caloriesGoal = 2000
                userSettings.fatGoal = 60
                userSettings.carbsGoal = 215
                userSettings.proteinGoal = 50
            }
            // Set text fields data
            amount_calories.setText(userSettings.caloriesGoal.toString())
            amount_fats.setText(userSettings.fatGoal.toString())
            amount_carbs.setText(userSettings.carbsGoal.toString())
            amount_protein.setText(userSettings.proteinGoal.toString())
        }

        // Upon save button click, Update values in firestore
        btn_setNutrients.setOnClickListener {
            userSettings.caloriesGoal = amount_calories.text.toString().toInt()
            userSettings.proteinGoal = amount_protein.text.toString().toInt()
            userSettings.fatGoal = amount_fats.text.toString().toInt()
            userSettings.carbsGoal = amount_carbs.text.toString().toInt()
            cloudData.addUserSettings(userId, userSettings)

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

    }
}