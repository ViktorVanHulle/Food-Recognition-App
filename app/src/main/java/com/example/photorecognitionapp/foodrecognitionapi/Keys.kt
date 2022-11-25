package com.example.photorecognitionapp.foodrecognitionapi

// Class to load defined C++ code in lib.cpp file
object Keys {
    init {
        System.loadLibrary("native-lib")
    }
    external fun apiKey(): String
}