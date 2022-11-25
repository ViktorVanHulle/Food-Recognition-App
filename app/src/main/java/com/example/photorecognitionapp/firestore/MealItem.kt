package com.example.photorecognitionapp.firestore

// Data class representing a MealItem
data class MealItem(
    var name: String? = "",
    var calories: Double? = 0.0,
    var fat: Double? = 0.0,
    var protein: Double? = 0.0,
    var carbohydrates: Double? = 0.0,
    var intakeQuan: Double? = 0.0,
    var date: String = ""
)
