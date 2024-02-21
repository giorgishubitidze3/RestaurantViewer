package com.example.restaurantviewer.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL ="https://api.yelp.com/v3/"
    private const val API_KEY ="Bearer QV7t355i_1p1rnBc9uibVZxx9N0JMkTh7nh5R9YP2CTdP6YhCEOwT27yRBVfzJzAQIDRzVlsgXMoVW_P92EtjnifAeYmaGkGta6DMdyaG0FT1PxC3t51IsBFRbPUZXYx"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()


    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}