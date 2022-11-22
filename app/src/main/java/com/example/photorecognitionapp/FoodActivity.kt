package com.example.photorecognitionapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.photorecognitionapp.firestore.CloudData
import com.example.photorecognitionapp.foodrecognitionapi.CmApi
import com.example.photorecognitionapp.foodrecognitionapi.btmRescale
import com.example.photorecognitionapp.foodrecognitionapi.btmpToByteArr

class FoodActivity : AppCompatActivity() {
    lateinit var img: Bitmap
    lateinit var userId: String
    lateinit var imgView: ImageView
    lateinit var protein: TextView
    lateinit var calcButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        imgView = findViewById(R.id.ivFood)
        protein = findViewById(R.id.proteinCount)
        calcButton = findViewById(R.id.calc)

        //retrieve extra intent data
        userId = intent.getStringExtra("userId").toString()
        img = intent.getParcelableExtra<Bitmap>("BitmapImage")!!

        val rescaledImg = btmRescale(img)
        imgView.setImageBitmap(rescaledImg)

        val cmApi = CmApi()
        val repsonse = cmApi.getImageData(btmpToByteArr(rescaledImg))
        println(repsonse.toString())


        calcButton.setOnClickListener {

        }

    }
}