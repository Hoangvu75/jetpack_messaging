package com.example.kotlinjetpack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinjetpack.view.auth.LoginAndRegisterActivity
import com.example.kotlinjetpack.view.bottom_navbar.BottomNavigationBarActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        startActivity(Intent(this, LoginAndRegisterActivity::class.java))
    }
}
