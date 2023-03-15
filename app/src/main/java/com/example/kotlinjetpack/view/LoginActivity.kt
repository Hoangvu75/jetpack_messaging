package com.example.kotlinjetpack.view

import com.example.kotlinjetpack.function.CustomDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.LoginViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current

    var loginFormWeight by remember { mutableStateOf(2f) }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            loginFormWeight = if (isKeyboardOpen) {
                4f
            } else {
                2f
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    var forgotPasswordText by remember { mutableStateOf("Forgot password?") }

    var phone by remember { mutableStateOf("0000000001") }
    var password by remember { mutableStateOf("0000000001") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val openDialog = rememberSaveable { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogContent by remember { mutableStateOf("") }
    val dialogFunction by remember { mutableStateOf({}) }
    if (openDialog.value) {
        CustomDialog(openDialogCustom = openDialog, dialogTitle, dialogContent, dialogFunction)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.3f))

        Text(
            text = "Message App",
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFFFFFFF), shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                )
                .weight(loginFormWeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Login",
                color = primaryColor,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = {
                    Text(text = "Phone")
                },
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    focusedLeadingIconColor = primaryColor,
                    focusedSupportingTextColor = primaryColor,
                    focusedTrailingIconColor = primaryColor,
                    cursorColor = primaryColor
                ),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(text = "Password")
                },
                leadingIcon = {
                    Icon(Icons.Filled.Password, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    focusedLeadingIconColor = primaryColor,
                    focusedSupportingTextColor = primaryColor,
                    focusedTrailingIconColor = primaryColor,
                    cursorColor = primaryColor
                ),
                shape = RoundedCornerShape(20.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )

            ClickableText(
                onClick = {
                    forgotPasswordText = "Who care?"
                },
                text = AnnotatedString(forgotPasswordText),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle(1),
                    textAlign = TextAlign.End,
                    color = primaryColor,
                ),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val loginViewModel = LoginViewModel()
                    loginViewModel.login(phone, password)
                    loginViewModel.resultLiveData.observe(lifecycleOwner) { result ->
                        when (result) {
                            0 -> {
                                println("Loading")
                            }
                            1 -> {
                                println("Success, token: ${loginViewModel.loginData!!.token}")
                                context.startActivity(Intent(context, BottomNavigationBarActivity::class.java))
                                activity?.finish()
                            }
                            2 -> {
                                println("Error: ${loginViewModel.errorMessage}")
                                dialogTitle = "Login Error"
                                dialogContent = loginViewModel.errorMessage!!
                                openDialog.value = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                ),
            ) {
                Text(
                    text = "Login",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Not have account yet? Sign up for now!",
                color = greyTextColor,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                    activity?.finish()
                },
                border = BorderStroke(1.dp, primaryColor),
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                Text(
                    text = "Register",
                    color = primaryColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}


