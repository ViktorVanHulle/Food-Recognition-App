package com.example.photorecognitionapp.firestore

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudData {
    private val db = Firebase.firestore
    private lateinit var mealArr: ArrayList<MealItem>

    fun getMealData(userId: String?) {
        if(userId != null) {
            mealArr = mutableListOf<MealItem>() as ArrayList<MealItem>
            db.collection(userId)
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            mealArr.add(document.toObject(MealItem::class.java))
                            //Log.d(TAG, document.id + " => " + document.data)
                        }
                        println(mealArr)
                    } else {
                        Log.d(ContentValues.TAG, "Error getting documents: ", task.exception)
                    }
                })
        }
    }

    private fun addMeal(userId: String, mealItem: MealItem){
        mealArr.add(mealItem)
        db.collection("users").document(userId)
            .set(mealArr)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }
}