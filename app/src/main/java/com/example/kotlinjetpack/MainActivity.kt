package com.example.kotlinjetpack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinjetpack.view.BottomNavigationBarActivity
import com.example.kotlinjetpack.view.ChatActivity
import com.example.kotlinjetpack.view.LoginActivity
import com.example.kotlinjetpack.view.RegisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
