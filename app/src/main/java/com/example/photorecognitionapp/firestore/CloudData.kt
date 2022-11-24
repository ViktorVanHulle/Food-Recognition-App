package com.example.photorecognitionapp.firestore

import android.content.ContentValues
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CloudData(): Parcelable {
    private val db = Firebase.firestore
    private var mealArr: MealList = MealList()

    constructor(parcel: Parcel) : this() {
    }

    fun addMeal(userId: String, mealItem: MealItem) {
        mealArr.mealList.add(mealItem)
        db.collection(userId).add(mealItem)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }


    fun addUserSettings(userId: String, userSettings: UserSettings) {
        db.collection(userId).document("userSettings").set(userSettings)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CloudData> {
        override fun createFromParcel(parcel: Parcel): CloudData {
            return CloudData(parcel)
        }

        override fun newArray(size: Int): Array<CloudData?> {
            return arrayOfNulls(size)
        }
    }
}

// Get current date as string DD-MM-YYYY format
@RequiresApi(Build.VERSION_CODES.O)
fun getDate(): String {
    val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return LocalDateTime.now().format(format)
}
