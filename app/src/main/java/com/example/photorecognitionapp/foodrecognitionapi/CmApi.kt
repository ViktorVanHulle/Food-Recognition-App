package com.example.photorecognitionapp.foodrecognitionapi

import android.graphics.Bitmap
import com.example.photorecognitionapp.firestore.MealItem
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException

class CmApi {
    private val apiKey = "secret"
    private var mealItem: MealItem? = null
    private var onRequestCompleteListener : OnRequestCompleteListener? = null

    fun getImageData(byteArr: ByteArray): MealItem? {
        val MEDIA_TYPE_JPEG = "image/jpeg".toMediaType()
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image", "image.jpeg", RequestBody.create("image/*jpeg".toMediaTypeOrNull(), byteArr))
            .build()

        val request = Request.Builder()
            .url("https://api-2445582032290.production.gw.apicast.io/v1/foodrecognition?user_key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    if (response.code != 200) {
                        println("Failed request")
                    } else {
                        val tempIdk = response.body!!.string()
                        val gson = Gson()
                        val mUser = gson.fromJson(tempIdk, JsonData.JsonDataMain::class.java)

                        if (!mUser.isFood!!) {
                            println("isFood: " + mUser.isFood)
                        } else {
                            //User input in grams
                            val userInput = 100
                            mealItem = MealItem(mUser.results[0].items[0].name,
                                mUser.results[0].items[0].nutrition!!.calories!! * 0.001,
                                mUser.results[0].items[0].nutrition!!.totalFat!! * 1000 * 0.001,
                                mUser.results[0].items[0].nutrition!!.totalCarbs!! * 1000 * 0.001,
                                mUser.results[0].items[0].nutrition!!.protein!! * 1000 * 0.001
                                )
                            return mealItem
                            /*
                            println(mUser.results[0].items[0].name)
                            println(mUser.results[0].items[0].nutrition!!.calories!! * 0.001 * userInput)
                            println(mUser.results[0].items[0].nutrition!!.totalFat!! * 1000 * 0.001 * userInput)
                            println(mUser.results[0].items[0].nutrition!!.totalCarbs!! * 1000 * 0.001 * userInput)
                            println(mUser.results[0].items[0].nutrition!!.protein!! * 1000 * 0.001 * userInput)
                             */
                        }
                        onRequestCompleteListener?.onSuccess(mealItem)
                    }
                }
            }
        })
    }
}


interface OnRequestCompleteListener{
    fun onSuccess(mealItem: MealItem?)
    fun onError()
}

// Rescale bitmap to 544x544
fun btmRescale(bitmap: Bitmap): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, 544, 544, true)
}

// bitmap to byte array
fun btmpToByteArr(bitmap: Bitmap): ByteArray{
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}
