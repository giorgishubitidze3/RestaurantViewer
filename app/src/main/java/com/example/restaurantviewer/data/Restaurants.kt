package com.example.restaurantviewer.data

import com.google.gson.annotations.SerializedName


data class Restaurants(
    val total: Int,
@SerializedName("businesses")
val businesses: List<YelpRestaurant>
)

data class YelpRestaurant(
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val image_url:String,
    @SerializedName("is_closed")
    val is_closed: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("review_count")
    val review_count: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("display_phone")
    val display_phone: String,
)
