package com.example.kotlinjetpack.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.kotlinjetpack.function.AppSettings

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val primaryColor = Color(123, 123, 255, 255)
val softPink = Color(255, 87, 199, 255)

@Composable
fun defaultTextColor(): Color {
    val isDarkMode by remember { AppSettings::isDarkMode }
    return if (isDarkMode) Color(0xFFF1F1F1) else Color(0xFF141414)
}

val greyTextColor = Color(0xFF7D7D7D)
val darkModeColor = Color(31, 31, 31)

