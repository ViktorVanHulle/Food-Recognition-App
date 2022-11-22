package com.example.photorecognitionapp.foodrecognitionapi

import android.graphics.Bitmap
import com.example.photorecognitionapp.firestore.MealItem
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.CountDownLatch

class CmApi {

    private var secretKey = Keys
    private val apiKey = secretKey.apiKey()
    private var mealItem: MealItem? = null

    fun getImageData(byteArr: ByteArray): MealItem? {
        val MEDIA_TYPE_JPEG = "image/jpeg".toMediaType()
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "image.jpeg",
                RequestBody.create("image/*jpeg".toMediaTypeOrNull(), byteArr)
            )
            .build()

        val request = Request.Builder()
            .url("https://api-2445582032290.production.gw.apicast.io/v1/foodrecognition?user_key=$apiKey")
            .post(requestBody)
            .build()

        var countDownLatch = CountDownLatch(1)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
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
                            mealItem = MealItem(mUser.results[0].items[0].name,
                                mUser.results[0].items[0].nutrition!!.calories!! * 0.001,
                                mUser.results[0].items[0].nutrition!!.totalFat!! * 1000 * 0.001,
                                mUser.results[0].items[0].nutrition!!.totalCarbs!! * 1000 * 0.001,
                                mUser.results[0].items[0].nutrition!!.protein!! * 1000 * 0.001
                                )
                        }
                    }
                }
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return mealItem
    }
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
