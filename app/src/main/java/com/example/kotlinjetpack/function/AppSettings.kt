package com.example.kotlinjetpack.function

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.kotlinjetpack.view_model.GetChatListViewModel
import com.example.kotlinjetpack.view_model.GetContactViewModel


// đây là nơi lưu các biến toàn cục của app
object AppSettings {
    var isDarkMode by mutableStateOf(false)

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}

