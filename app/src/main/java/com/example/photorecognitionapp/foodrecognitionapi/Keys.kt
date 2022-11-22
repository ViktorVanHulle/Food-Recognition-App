package com.example.photorecognitionapp.foodrecognitionapi

object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}