package com.example.kotlinjetpack.view.auth.login_packages

import android.app.Activity
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner

@Composable
fun LoginForm(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    activity: Activity,
    onRegisterClicked: () -> Unit
) {
    var phone by remember { mutableStateOf("0941290612") }
    var password by remember { mutableStateOf("0941290612") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                Color(0xFFFFFFFF), shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        LoginClass().LoginText()

        Spacer(modifier = Modifier.height(20.dp))

        LoginClass().PhoneTextField(phone = phone, onPhoneChange = { phone = it })

        Spacer(modifier = Modifier.height(16.dp))

        LoginClass().PasswordTextField(password = password, onPasswordChange = { password = it })

        LoginClass().ForgotPasswordText()

        Spacer(modifier = Modifier.height(20.dp))

        LoginClass().LoginButton(
            phone = phone,
            password = password,
            lifecycleOwner = lifecycleOwner,
            context = context,
            activity = activity
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginClass().SubtitleRegister()

        Spacer(modifier = Modifier.height(10.dp))

        LoginClass().RegisterButton(
            onRegisterClicked = {
                onRegisterClicked()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}