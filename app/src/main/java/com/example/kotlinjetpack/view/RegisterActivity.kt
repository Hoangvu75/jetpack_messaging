package com.example.kotlinjetpack.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinjetpack.function.CustomDialog
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.RegisterViewModel
import java.util.*

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen()
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current

    var registerFormWeight by remember { mutableStateOf(2f) }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            registerFormWeight = if (isKeyboardOpen) {
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

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reEnterPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
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
                dateOfBirth = "$mDayOfMonth/${mMonth + 1}/$mYear"
            }, year, month, day
        )
        datePickerDialog.show()
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val openAlertDialog = rememberSaveable { mutableStateOf(false) }
    var alertDialogTitle by remember { mutableStateOf("") }
    var alertDialogContent by remember { mutableStateOf("") }
    var alertDialogFunction by remember { mutableStateOf({}) }
    if (openAlertDialog.value) {
        CustomDialog(openDialogCustom = openAlertDialog, alertDialogTitle, alertDialogContent, alertDialogFunction)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
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
                .verticalScroll(rememberScrollState())
                .weight(registerFormWeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Register",
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

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reEnterPassword,
                onValueChange = { reEnterPassword = it },
                label = {
                    Text(text = "Re-enter password")
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

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(text = "Name")
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = {
                    Text(text = "Date of birth")
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

            Spacer(modifier = Modifier.height(30.dp))

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
                                alertDialogFunction = {
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                    activity?.finish()
                                }
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
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                ),
            ) {
                Text(
                    text = "Register",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Already have account? Sign in.",
                color = greyTextColor,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                },
                border = BorderStroke(1.dp, primaryColor),
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                Text(
                    text = "Login",
                    color = primaryColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}