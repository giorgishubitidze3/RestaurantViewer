package com.example.restaurantviewer.network

import com.example.restaurantviewer.data.Restaurants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("businesses/search")
   suspend fun searchRestaurants(
        @Query("location") location: String
    ): Response<Restaurants>
}