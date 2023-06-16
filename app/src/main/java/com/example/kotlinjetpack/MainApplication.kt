package com.example.kotlinjetpack

import android.app.Application
import android.widget.Toast
import live.videosdk.rtc.android.VideoSDK

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupVideoSDK()
    }

    private fun setupVideoSDK() {
        try {
            VideoSDK.initialize(applicationContext)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}