package com.example.photorecognitionapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider


class CameraActivity : AppCompatActivity() {


   companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        //private const val CAMERA_REQUEST_CODE = 2
   }

    //get references
    val btn_camera = findViewById<Button>(R.id.btn_camera) as Button;
    private val imageView:ImageView by lazy {
        findViewById<ImageView>(R.id.imageView) as ImageView
    }

    //Camera launcher
    private var tempImageUri: Uri? = null
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            succes ->

        if(succes){
            imageView.setImageURI(tempImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        //set on-click listener
        btn_camera.setOnClickListener() {

            //set temp imageView URI
            tempImageUri = Uri.parse("android.resource://com.example.photorecognitionapp/drawable/ic_launcher_foreground.xml")

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            ){
                cameraLauncher.launch(tempImageUri)
            }else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cameraLauncher.launch(tempImageUri)
            }
            else{
                Toast.makeText(
                    this,
                    "Permission for camera denied. Allow camera permission to continue.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}