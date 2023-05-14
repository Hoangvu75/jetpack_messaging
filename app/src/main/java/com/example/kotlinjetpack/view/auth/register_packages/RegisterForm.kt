package com.example.kotlinjetpack.view.auth.register_packages

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterForm(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    onAlertDialogClick: () -> Unit,
    onLoginClicked: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reEnterPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    val overScrollController = ScrollableDefaults.overscrollEffect()

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
            .animateContentSize()
            .overscroll(overscrollEffect = overScrollController)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        RegisterClass().RegisterText()

        Spacer(modifier = Modifier.height(20.dp))

        RegisterClass().PhoneTextField(phone = phone, onPhoneChange = { phone = it })

        Spacer(modifier = Modifier.height(16.dp))

        RegisterClass().PasswordTextField(password = password, onPasswordChange = { password = it })

        Spacer(modifier = Modifier.height(16.dp))

        RegisterClass().ReEnterPasswordTextField(
            reEnterPassword = reEnterPassword,
            onReEnterPasswordChange = {
                reEnterPassword = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterClass().NameTextField(name = name, onNameChange = { name = it })

        Spacer(modifier = Modifier.height(16.dp))

        RegisterClass().BirthdayField(
            dateOfBirth = dateOfBirth,
            onDateChange = {
                dateOfBirth = it
            },
            context = context
        )

        Spacer(modifier = Modifier.height(30.dp))

        RegisterClass().RegisterButton(
            phone = phone,
            password = password,
            reEnterPassword = reEnterPassword,
            name = name,
            dateOfBirth = dateOfBirth,
            lifecycleOwner = lifecycleOwner,
            alertDialogFunction = {
                onAlertDialogClick()
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        RegisterClass().SubtitleLogin()

        Spacer(modifier = Modifier.height(10.dp))

        RegisterClass().LoginButton(
            onLoginClicked = {
                onLoginClicked()
            }
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}