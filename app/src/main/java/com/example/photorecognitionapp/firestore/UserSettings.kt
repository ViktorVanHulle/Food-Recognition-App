package com.example.photorecognitionapp.firestore

// Data class representing user settings
data class UserSettings(
    var proteinGoal: Int = 0,
    var carbsGoal: Int = 0,
    var fatGoal: Int = 0,
    var caloriesGoal: Int = 0
    )