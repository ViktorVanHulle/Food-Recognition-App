package com.example.photorecognitionapp.foodrecognitionapi

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.photorecognitionapp.firestore.MealItem
import com.example.photorecognitionapp.firestore.getDate
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.CountDownLatch

class CmApi {
    // Initialize api key and a MealItem
    private var secretKey = Keys
    private val apiKey = secretKey.apiKey()
    private var mealItem: MealItem? = null

    /* Constructs a http request with attached image as bytearray given,
    and sends the request to Calorie Mama API. Returns a MealItem object
     */
    fun getImageData(byteArr: ByteArray): MealItem? {
        val client = OkHttpClient()

        // Create request body
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "image.jpeg",
                RequestBody.create("image/*jpeg".toMediaTypeOrNull(), byteArr)
            )
            .build()

        // build request
        val request = Request.Builder()
            .url("https://api-2445582032290.production.gw.apicast.io/v1/foodrecognition?user_key=$apiKey")
            .post(requestBody)
            .build()

        // Creates a counter used to ensure execution of request
        val countDownLatch = CountDownLatch(1)

        // Execute request asynchronous.
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                e.printStackTrace()
            }

            // On response of the request, parse data with gson into temporary object and get desired data to change the MealItem object.
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    if (response.code != 200) {
                        println("Failed request")
                    } else {
                        val respData = response.body!!.string()
                        val gson = Gson()
                        val mItem = gson.fromJson(respData, JsonData.JsonDataMain::class.java)

                        if (!mItem.isFood!!) {
                            println("isFood: " + mItem.isFood)
                        } else {
                            mealItem = MealItem(mItem.results[0].items[0].name,
                                mItem.results[0].items[0].nutrition!!.calories!! * 0.001,
                                mItem.results[0].items[0].nutrition!!.totalFat!! * 1000 * 0.001,
                                mItem.results[0].items[0].nutrition!!.protein!! * 1000 * 0.001,
                                mItem.results[0].items[0].nutrition!!.totalCarbs!! * 1000 * 0.001,
                                0.0,
                                getDate()
                                )
                        }
                    }
                }
                countDownLatch.countDown()
            }
        })
        // Await execution of either OnResponse or OnFailure to ensure the request finished before returning.
        countDownLatch.await()
        return mealItem
    }
}

// Rescale bitmap to 544x544
fun btmRescale(bitmap: Bitmap): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, 544, 544, true)
}

// Bitmap to byte array
fun btmpToByteArr(bitmap: Bitmap): ByteArray{
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}
