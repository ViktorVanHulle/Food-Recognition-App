package com.example.photorecognitionapp.firestore

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudData(): Parcelable {
    private val db = Firebase.firestore
    private var mealArr: MealList = MealList()

    constructor(parcel: Parcel) : this() {

    }

    fun getMealData(userId: String?) {
        if(userId != null) {
            db.collection(userId)
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            mealArr.mealList.add(document.toObject(MealItem::class.java))
                            //Log.d(TAG, document.id + " => " + document.data)
                        }
                        println(mealArr)
                    } else {
                        Log.d(ContentValues.TAG, "Error getting documents: ", task.exception)
                    }
                })
        }
    }

    fun addMeal(userId: String, mealItem: MealItem) {
        mealArr.mealList.add(mealItem)
        db.collection(userId).add(mealItem)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }

    fun getMealArr(): ArrayList<MealItem> {
        return mealArr.mealList
    }

    fun addMealArr(mealItem: MealItem) {
        mealArr.mealList.add(mealItem)
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