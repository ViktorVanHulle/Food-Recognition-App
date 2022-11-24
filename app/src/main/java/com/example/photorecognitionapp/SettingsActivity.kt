package com.example.photorecognitionapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SettingsActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //listen if button is clicked
        clickListener()
    }

    private fun clickListener(){
        val btn_setCalories = findViewById<Button>(R.id.btn_setCalories)
        val btn_setProtein = findViewById<Button>(R.id.btn_setProtein)
        val btn_setFats = findViewById<Button>(R.id.btn_setFats)
        val btn_setCarbs = findViewById<Button>(R.id.btn_setCarbs)

        btn_setCalories.setOnClickListener {

        }
        btn_setProtein.setOnClickListener {

        }
        btn_setFats.setOnClickListener {

        }
        btn_setCarbs.setOnClickListener {

        }

    }
}