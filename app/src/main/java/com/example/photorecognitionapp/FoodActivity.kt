package com.example.photorecognitionapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
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
    lateinit var itemQuan: EditText
    lateinit var foodName: TextView
    lateinit var addFoodButton: Button
    lateinit var cloudData: CloudData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        imgView = findViewById(R.id.ivFood)
        protein = findViewById(R.id.proteinCount)
        calcButton = findViewById(R.id.calc)
        itemQuan = findViewById(R.id.itemQuanEt)
        foodName = findViewById(R.id.txFoodName)
        addFoodButton = findViewById(R.id.addFoodBtn)

        //retrieve extra intent data
        userId = intent.getStringExtra("userId").toString()
        img = intent.getParcelableExtra<Bitmap>("BitmapImage")!!
        cloudData = intent.getParcelableExtra<CloudData>("cloudData")!!


        val rescaledImg = btmRescale(img)
        imgView.setImageBitmap(rescaledImg)

        val cmApi = CmApi()
        val mealItem = cmApi.getImageData(btmpToByteArr(rescaledImg))

        foodName.setText(mealItem?.name.toString())

        calcButton.setOnClickListener {
            if (mealItem != null) {
                var proteinTemp = mealItem.protein?.times(itemQuan.text.toString().toDouble())
                protein.setText(proteinTemp.toString())
            }
        }

        addFoodButton.setOnClickListener {
            // Add to cloud storage
            if (mealItem != null) {
                cloudData.addMeal(userId, mealItem)
            }

            // Calculate daily values
            // Store in memory for the session

            // return to dashboard
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("cloudData", cloudData)
            startActivity(intent)
        }


    }
}