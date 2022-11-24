package com.example.photorecognitionapp.firestore

data class UserSettings(
    var proteinGoal: Int = 0,
    var carbsGoal: Int = 0,
    var fatGoal: Int = 0,
    var caloriesGoal: Int = 0
    )