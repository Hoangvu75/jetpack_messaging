package com.example.kotlinjetpack.retrofit

import com.example.kotlinjetpack.const.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val baseUrl = BASE_URL

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstanceVideoSdk(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.videosdk.live/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}