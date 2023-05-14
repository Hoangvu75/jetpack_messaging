package com.example.kotlinjetpack.view.auth.login_packages

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.kotlinjetpack.function.CustomDialog
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view.bottom_navbar.BottomNavigationBarActivity
import com.example.kotlinjetpack.view_model.LoginViewModel
import kotlinx.coroutines.delay

class LoginClass {

    @Composable
    fun LoginText() {
        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 500
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { - 200.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
            Text(
                text = "Login",
                color = primaryColor,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhoneTextField(
        phone: String,
        onPhoneChange: (String) -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 1000
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { 200.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
            OutlinedTextField(
                value = phone,
                onValueChange = { onPhoneChange(it) },
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
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordTextField(
        password: String,
        onPasswordChange: (String) -> Unit
    ) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 1500
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { - 200.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
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
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }
    }

    @Composable
    fun ForgotPasswordText() {
        var forgotPasswordText by remember { mutableStateOf("Forgot password?") }

        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 1500
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
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
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }
    }

    @Composable
    fun LoginButton(
        phone: String,
        password: String,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        activity: Activity
    ) {
        val openDialog = rememberSaveable { mutableStateOf(false) }
        var dialogTitle by remember { mutableStateOf("") }
        var dialogContent by remember { mutableStateOf("") }
        val dialogFunction by remember { mutableStateOf({}) }
        if (openDialog.value) {
            CustomDialog(openDialogCustom = openDialog, dialogTitle, dialogContent, dialogFunction)
        }

        var buttonText by remember { mutableStateOf("") }

        var buttonState by remember { mutableStateOf(false) }
        val duration = 1000
        val delay = 2000
        val scale: Float by animateFloatAsState(
            targetValue = if (buttonState) 0.8f else 0f,
            animationSpec = tween(durationMillis = duration, delayMillis = delay),
            finishedListener = {
                buttonText = "Login"
            }
        )

        Box(
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(20.dp))
        ) {
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
                                context.startActivity(
                                    Intent(
                                        context,
                                        BottomNavigationBarActivity::class.java
                                    )
                                )
                                activity.finish()
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
                modifier = Modifier.fillMaxWidth(scale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                ),
            ) {
                Text(
                    text = buttonText,
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.titleLarge
                )
            }.let { animation ->
                LaunchedEffect(animation) {
                    buttonState = true
                }
            }
        }
    }

    @Composable
    fun SubtitleRegister() {
        var subtitle by remember { mutableStateOf("") }
        val subtitleFinal = "Not have account yet? Sign up for now!"
        val delay = 2500

        Text(
            text = subtitle,
            color = greyTextColor,
            style = MaterialTheme.typography.bodyLarge
        ).let { animation ->
            LaunchedEffect(animation) {
                delay(delay.toLong())
                subtitleFinal.forEachIndexed { index, _ ->
                    delay(20)
                    subtitle = subtitleFinal.substring(0, index + 1)
                }
            }
        }
    }

    @Composable
    fun RegisterButton(
        onRegisterClicked: () -> Unit
    ) {
        var buttonText by remember { mutableStateOf("") }

        var buttonState by remember { mutableStateOf(false) }
        val duration = 1000
        val delay = 3000
        val scale: Float by animateFloatAsState(
            targetValue = if (buttonState) 0.8f else 0f,
            animationSpec = tween(durationMillis = duration, delayMillis = delay),
            finishedListener = {
                buttonText = "Register"
            }
        )

        Box(
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(20.dp))
        ) {
            OutlinedButton(
                onClick = {
                    onRegisterClicked()
                },
                border = BorderStroke(1.dp, primaryColor),
                modifier = Modifier.fillMaxWidth(scale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                Text(
                    text = buttonText,
                    color = primaryColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }.let { animation ->
                LaunchedEffect(animation) {
                    buttonState = true
                }
            }
        }
    }
}
