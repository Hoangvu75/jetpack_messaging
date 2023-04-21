package com.example.kotlinjetpack.view

import android.os.Process.killProcess
import android.os.Process.myPid
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpack.ui.theme.defaultTextColor

@Composable
fun SettingsScreen() {
    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.displayMedium.copy(
                        textAlign = TextAlign.Start,
                        color = defaultTextColor,
                    ),
                )

                Button(
                    onClick = {
                        killProcess(myPid())
                    },
                    modifier = Modifier
                        .padding(top = 300.dp)
                        .fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = "Logout",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Start,
                            color = defaultTextColor,
                        ),
                    )
                }
            }
        }
    }
}

