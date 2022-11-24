package com.example.photorecognitionapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.photorecognitionapp.firestore.CloudData
import com.example.photorecognitionapp.foodrecognitionapi.CmApi
import com.example.photorecognitionapp.foodrecognitionapi.btmRescale
import com.example.photorecognitionapp.foodrecognitionapi.btmpToByteArr
import java.text.DecimalFormat

class FoodActivity : AppCompatActivity() {
    lateinit var img: Bitmap
    lateinit var userId: String
    lateinit var imgView: ImageView
    lateinit var protein: TextView
    lateinit var fat: TextView
    lateinit var carbs: TextView
    lateinit var itemQuan: EditText
    lateinit var foodName: TextView
    lateinit var addFoodButton: Button
    lateinit var cloudData: CloudData

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        imgView = findViewById(R.id.ivFood)
        protein = findViewById(R.id.proteinCount)
        fat = findViewById(R.id.fatCount)
        carbs = findViewById(R.id.carbsCount)
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

        val protein100 = mealItem?.protein?.times(100)
        val fat100 = mealItem?.fat?.times(100)
        val carbs100 = mealItem?.carbohydrates?.times(100)


        val df = DecimalFormat("#.##")
        foodName.text = mealItem?.name.toString()
        protein.text = df.format(protein100).toString()
        fat.text = df.format(fat100).toString()
        carbs.text = df.format(carbs100).toString()


        addFoodButton.setOnClickListener {
            // Add to cloud storage
            if (mealItem != null) {
                mealItem.intakeQuan = itemQuan.text.toString().toDouble()
                cloudData.addMeal(userId, mealItem)
            }

            // return to dashboard
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("cloudData", cloudData)

            startActivity(intent)
        }
    }
}