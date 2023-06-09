package com.example.kotlinjetpack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.geometry.Size
import com.example.kotlinjetpack.utils.responsiveSize
import com.example.kotlinjetpack.view.auth.LoginAndRegisterActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        build(context = this)
        print("dcm")

        finish()
        startActivity(Intent(this, LoginAndRegisterActivity::class.java))
    }
}

fun build(context: Context) {
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels

    val designWidth = 1080 // Design width
    val designHeight = 2280 // Design height

    val responsiveWidth = screenWidth.toFloat() / designWidth
    val responsiveHeight = screenHeight.toFloat() / designHeight
    val mResponsiveSize = Size(responsiveWidth, responsiveHeight)
    responsiveSize = mResponsiveSize

    print("res_width: ${responsiveSize.width} res_height: ${responsiveSize.height}")

}