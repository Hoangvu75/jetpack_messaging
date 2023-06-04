package com.example.kotlinjetpack.function

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpack.view_model.GetChatListViewModel
import com.example.kotlinjetpack.view_model.GetContactViewModel

// đây là nơi lưu các biến toàn cục của app
object AppSettings {
    var isDarkMode by mutableStateOf(false)
    var getChatListViewModel: GetChatListViewModel = GetChatListViewModel()
    var getContactViewModel: GetContactViewModel = GetContactViewModel()
}

