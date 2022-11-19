package com.example.photorecognitionapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class ViewActivity : AppCompatActivity() {

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        //getId
        imageView = findViewById<ImageView>(R.id.imageView)

        //retrieve image
        val intent = intent
        val bitmap = intent.getParcelableExtra<Parcelable>("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)

        //listen if category is clicked
        clickListener()
    }

    public fun clickListener(){
        var imageVegetable = findViewById<CardView>(R.id.ivVegetable)
        var imageFruit = findViewById<CardView>(R.id.ivFruit)
        var imageFish = findViewById<CardView>(R.id.ivFish)
        var imageMeat = findViewById<CardView>(R.id.ivMeat)
        var imageDairy = findViewById<CardView>(R.id.ivDairy)
        var imageBakery = findViewById<CardView>(R.id.ivBakery)

        imageVegetable.setOnClickListener {
            openCategoryActivity("Vegetable");
        }
        imageFruit.setOnClickListener {
            openCategoryActivity("Fruit");
        }
        imageFish.setOnClickListener {
            openCategoryActivity("Fish");
        }
        imageMeat.setOnClickListener {
            openCategoryActivity("Meat");
        }
        imageDairy.setOnClickListener {
            openCategoryActivity("Dairy");
        }
        imageBakery.setOnClickListener {
            openCategoryActivity("Bakery");
        }


    }

    public fun openCategoryActivity(category:String){
        //set category clicked
        //create intent and pass the category
        val intent = Intent(this, FoodActivity::class.java)
        intent.putExtra("CategoryFood", category)
        //start viewActivity
        startActivity(intent)
    }

}