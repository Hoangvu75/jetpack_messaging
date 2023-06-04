package com.example.kotlinjetpack.function

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kotlinjetpack.ui.theme.primaryColor

// đây là dialog tự custom giao diện

// hàm này gọi dialog, dialog sẽ hiển thị khi openDialogCustom = true
@Composable
fun CustomDialog(
    openDialogCustom: MutableState<Boolean>,
    title: String,
    content: String,
    function: () -> (Unit)
) {
    Dialog(onDismissRequest = {}) {
        CustomDialogUI(
            openDialogCustom = openDialogCustom, title = title, content = content,
            function = function
        )
    }
}

// đây là giao diện của dialog
@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    title: String,
    content: String,
    function: () -> (Unit)
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
    ) {
        Column(
            modifier
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(primaryColor),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = {
                    openDialogCustom.value = false
                    function()
                }) {
                    Text(
                        "Confirm",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
