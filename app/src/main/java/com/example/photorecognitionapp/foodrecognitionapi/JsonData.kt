package com.example.photorecognitionapp.foodrecognitionapi

import com.google.gson.annotations.SerializedName

// Temporary data classes created using an online Json to Kotlin converter tool
class JsonData {
    data class JsonDataMain (
        @SerializedName("is_food"       ) var isFood       : Boolean?           = null,
        @SerializedName("_timing"       ) var Timing       : Timing?            = Timing(),
        @SerializedName("lang"          ) var lang         : String?            = null,
        @SerializedName("results"       ) var results      : ArrayList<Results> = arrayListOf(),
        @SerializedName("imagecache_id" ) var imagecacheId : String?            = null
    )

    data class Results (
        @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf(),
        @SerializedName("group" ) var group : String?          = null
    )

    data class Items (
        @SerializedName("servingSizes" ) var servingSizes : ArrayList<ServingSizes> = arrayListOf(),
        @SerializedName("score"        ) var score        : Int?                    = null,
        @SerializedName("nutrition"    ) var nutrition    : Nutrition?              = Nutrition(),
        @SerializedName("name"         ) var name         : String?                 = null,
        @SerializedName("food_id"      ) var foodId       : String?                 = null,
        @SerializedName("group"        ) var group        : String?                 = null

    )

    data class Nutrition (
        @SerializedName("totalCarbs" ) var totalCarbs : Double? = null,
        @SerializedName("totalFat"   ) var totalFat   : Double? = null,
        @SerializedName("protein"    ) var protein    : Double? = null,
        @SerializedName("calories"   ) var calories   : Double? = null
    )

    data class ServingSizes (
        @SerializedName("unit"          ) var unit          : String? = null,
        @SerializedName("servingWeight" ) var servingWeight : Double? = null
    )

    data class Timing (
        @SerializedName("foodai_totaltime"          ) var foodaiTotaltime          : Double? = null,
        @SerializedName("foodai_classificationtime" ) var foodaiClassificationtime : Double? = null,
        @SerializedName("proxy_foodairequesttime"   ) var proxyFoodairequesttime   : Double? = null
    )
}