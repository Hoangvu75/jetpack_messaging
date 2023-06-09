package com.example.kotlinjetpack.view.auth.register_packages

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.LifecycleOwner
import com.example.kotlinjetpack.function.CustomDialog
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.RegisterViewModel
import kotlinx.coroutines.delay
import java.util.*

class RegisterClass {

    @Composable
    fun RegisterText() {
        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 500
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { -200.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
            Text(
                text = "Register",
                color = primaryColor,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 10.em
                ),
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
                    Text(
                        text = "Phone",
                        style = TextStyle(
                            fontSize = 4.em
                        )
                    )
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
                with(density) { -200.dp.roundToPx() }
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
                    Text(
                        text = "Password",
                        style = TextStyle(
                            fontSize = 4.em
                        )
                    )
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReEnterPasswordTextField(
        reEnterPassword: String,
        onReEnterPasswordChange: (String) -> Unit
    ) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                value = reEnterPassword,
                onValueChange = { onReEnterPasswordChange(it) },
                label = {
                    Text(
                        text = "Re-enter password",
                        style = TextStyle(
                            fontSize = 4.em
                        )
                    )
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NameTextField(
        name: String,
        onNameChange: (String) -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        val duration = 1000
        val delay = 1500
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            ) {
                with(density) { -200.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
                initialAlpha = 0.3f
            )
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                label = {
                    Text(
                        text = "Name",
                        style = TextStyle(
                            fontSize = 4.em
                        )
                    )
                },
                leadingIcon = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BirthdayField(
        dateOfBirth: String,
        onDateChange: (String) -> Unit,
        context: Context
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        if (interactionSource.collectIsPressedAsState().value) {
            val calendar = Calendar.getInstance()

            calendar.time = Date()
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    onDateChange("$mDayOfMonth/${mMonth + 1}/$mYear")
                }, year, month, day
            )
            datePickerDialog.show()
        }

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
                value = dateOfBirth,
                onValueChange = { onDateChange(it) },
                label = {
                    Text(
                        text = "Date of birth",
                        style = TextStyle(
                            fontSize = 4.em
                        )
                    )
                },
                leadingIcon = {
                    Icon(Icons.Filled.EditCalendar, contentDescription = null)
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
                readOnly = true,
                interactionSource = interactionSource
            )
        }.let { animation ->
            LaunchedEffect(animation) {
                visible = true
            }
        }
    }

    @Composable
    fun RegisterButton(
        phone: String,
        password: String,
        reEnterPassword: String,
        name: String,
        dateOfBirth: String,
        lifecycleOwner: LifecycleOwner,
        alertDialogFunction: () -> Unit
    ) {
        val openAlertDialog = rememberSaveable { mutableStateOf(false) }
        var alertDialogTitle by remember { mutableStateOf("") }
        var alertDialogContent by remember { mutableStateOf("") }
        if (openAlertDialog.value) {
            CustomDialog(
                openDialogCustom = openAlertDialog,
                alertDialogTitle,
                alertDialogContent,
                alertDialogFunction
            )
        }

        var buttonText by remember { mutableStateOf("") }

        var buttonState by remember { mutableStateOf(false) }
        val duration = 1000
        val delay = 2000
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
            Button(
                onClick = {
                    val registerViewModel = RegisterViewModel()
                    registerViewModel.register(phone, password, reEnterPassword, name, dateOfBirth)
                    registerViewModel.resultLiveData.observe(lifecycleOwner) { result ->
                        when (result) {
                            0 -> {
                                println("Loading")
                            }

                            1 -> {
                                println("Success, account: ${registerViewModel.registerData!!.registeredAccount}")
                                println("Success, profile: ${registerViewModel.addProfileData!!.addedProfile}")
                                alertDialogTitle = "Register Success"
                                alertDialogContent = "Login and enjoy now"
                                alertDialogFunction()
                                openAlertDialog.value = true
                            }

                            2 -> {
                                println("Error: ${registerViewModel.errorMessage}")
                                alertDialogTitle = "Register Error"
                                alertDialogContent = registerViewModel.errorMessage!!
                                openAlertDialog.value = true
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
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 6.em
                    )
                )
            }.let { animation ->
                LaunchedEffect(animation) {
                    buttonState = true
                }
            }
        }
    }

    @Composable
    fun SubtitleLogin() {
        var subtitle by remember { mutableStateOf("") }
        val subtitleFinal = "Already have account? Please sign in."

        val delay = 2500

        Text(
            text = subtitle,
            color = greyTextColor,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 4.em
            )
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
    fun LoginButton(
        onLoginClicked: () -> Unit
    ) {
        var buttonText by remember { mutableStateOf("") }

        var buttonState by remember { mutableStateOf(false) }
        val duration = 1000
        val delay = 3000
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
            OutlinedButton(
                onClick = {
                    onLoginClicked()
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
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 6.em
                    )
                )
            }.let { animation ->
                LaunchedEffect(animation) {
                    buttonState = true
                }
            }
        }

    }
}
