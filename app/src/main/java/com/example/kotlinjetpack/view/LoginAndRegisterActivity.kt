package com.example.kotlinjetpack.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.example.kotlinjetpack.function.CustomDialog
import com.example.kotlinjetpack.function.DialogBoxLoading
import com.example.kotlinjetpack.function.shadow
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view.bottom_navbar.BottomNavigationBarActivity
import com.example.kotlinjetpack.view_model.LoginAndRegisterViewModel
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Date

@Suppress("DEPRECATION")
class LoginAndRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RenderLoginAndRegister()
        }
    }

    @Composable
    fun RenderLoginAndRegister(
        focusManager: FocusManager = LocalFocusManager.current,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryColor)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TopLayout().RenderTopLayout()

            BottomLayout().RenderBottomLayout(modifier = Modifier.weight(1f))
        }
    }

    inner class TopLayout {
        @Composable
        fun RenderTopLayout(
            view: View = LocalView.current,
            density: Density = LocalDensity.current,
            visible: MutableState<Boolean> = remember { mutableStateOf(false) },
            duration: Int = 500,
            delay: Int = 0
        ) {
            DisposableEffect(view) {
                val listener = ViewTreeObserver.OnGlobalLayoutListener {
                    val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                        ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
                    visible.value = !isKeyboardOpen
                }

                view.viewTreeObserver.addOnGlobalLayoutListener(listener)
                onDispose {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }

            Column(
                modifier = Modifier.animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    ) {
                        with(density) { 500.dp.roundToPx() }
                    } + expandVertically(
                        animationSpec = tween(durationMillis = duration, delayMillis = delay),
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = duration, delayMillis = delay),
                        initialAlpha = 0f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier.padding(
                            vertical = 50.dp
                        )
                    ) {
                        RenderTopLayoutTitle()
                    }
                }
            }.let { animation ->
                LaunchedEffect(animation) {
                    visible.value = true
                }
            }
        }

        @Composable
        fun RenderTopLayoutTitle(
            state: MutableState<Boolean> = remember { mutableStateOf(false) },
            duration: Int = 1000,
            delay: Int = 0,
            scale: State<Float> = animateFloatAsState(
                targetValue = if (!state.value) 0f else 11f,
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            )
        ) {
            Text(
                text = "Messaging App",
                color = Color(0xFFFFFFFF),
                style = TextStyle(
                    fontSize = scale.value.em
                )
            ).let { animation ->
                LaunchedEffect(animation) {
                    state.value = true
                }
            }
        }
    }

    inner class BottomLayout {
        private val viewModel = LoginAndRegisterViewModel()

        private var bottomLoginVisible = MutableLiveData(false)
        private var bottomRegisterVisible = MutableLiveData(false)

        @Composable
        fun RenderBottomLayout(
            modifier: Modifier,
        ) {
            Column(
                modifier = modifier,
            ) {
                RenderAnimatedForm(
                    visibleLiveData = bottomLoginVisible,
                    modifier = Modifier.weight(1f),
                ) {
                    LoginModule().RenderLoginForm()
                }.let { animation ->
                    LaunchedEffect(animation) {
                        bottomLoginVisible.value = !bottomLoginVisible.value!!
                    }
                }

                RenderAnimatedForm(
                    visibleLiveData = bottomRegisterVisible,
                    modifier = Modifier.weight(1f),
                ) {
                    RegisterModule().RenderRegisterForm()
                }
            }
        }

        @Composable
        fun RenderAnimatedForm(
            modifier: Modifier = Modifier,
            density: Density = LocalDensity.current,
            visibleLiveData: MutableLiveData<Boolean>,
            visible: Boolean = visibleLiveData.observeAsState().value!!,
            duration: Int = 750,
            delay: Int = 0,
            content: @Composable () -> Unit,
        ) {
            AnimatedVisibility(
                visible = visible,
                modifier = modifier,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    initialOffsetY = {
                        with(density) { 500.dp.roundToPx() }
                    }
                ) + expandVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    initialAlpha = 0f
                ),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    targetOffsetY = {
                        with(density) { 500.dp.roundToPx() }
                    }
                ) + shrinkVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ) + fadeOut(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            borderRadius = 10.dp,
                            offsetY = 10.dp,
                            spread = 10.dp,
                            blurRadius = 10.dp
                        )
                ) {
                    content()
                }
            }
        }

        inner class LoginModule {
            private var phone by mutableStateOf("0941290612")
            private var password by mutableStateOf("0941290612")

            @Composable
            fun RenderLoginForm() {
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

                    RenderLoginTitle()

                    Spacer(modifier = Modifier.height(20.dp))

                    RenderPhoneTextField()

                    Spacer(modifier = Modifier.height(16.dp))

                    RenderPasswordTextField()

                    RenderForgotPasswordText()

                    Spacer(modifier = Modifier.height(20.dp))

                    RenderLoginButton()

                    Spacer(modifier = Modifier.height(20.dp))

                    RenderSubtitleRegister()

                    Spacer(modifier = Modifier.height(10.dp))

                    RenderRegisterButton()

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            @Composable
            fun RenderLoginTitle(
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 500,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        text = "Login",
                        color = primaryColor,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 10.em
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderPhoneTextField(
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1000,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { phone = it },
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
                        visible.value = true
                    }
                }

            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderPasswordTextField(
                passwordVisible: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1000,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { password = it },
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
                        visualTransformation = if (passwordVisible.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible.value) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }

                            val description =
                                if (passwordVisible.value) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible.value = true
                    }
                }
            }

            @Composable
            fun RenderForgotPasswordText(
                forgotPasswordText: MutableState<String> = remember {
                    mutableStateOf(
                        "Forgot password?"
                    )
                },
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1500,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                            forgotPasswordText.value = "Who care?"
                        },
                        text = AnnotatedString(forgotPasswordText.value),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = 4.em,
                            textAlign = TextAlign.End,
                            color = primaryColor,
                        ),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(0.8f)
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible.value = true
                    }
                }
            }

            @Composable
            fun RenderLoginButton(
                context: Context = LocalContext.current,
                activity: Activity = ((context as? Activity)!!),
                openAlertDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                alertDialogTitle: MutableState<String> = remember { mutableStateOf("") },
                alertDialogContent: MutableState<String> = remember { mutableStateOf("") },
                alertDialogFunction: MutableState<() -> Unit> = remember { mutableStateOf({}) },
                openLoadingDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                buttonText: MutableState<String> = remember { mutableStateOf("") },
                state: MutableState<Boolean> = remember { mutableStateOf(false) },
                duration: Int = 1000,
                delay: Int = 2000,
                scale: State<Float> = animateFloatAsState(
                    targetValue = if (state.value) 0.8f else 0f,
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    finishedListener = {
                        buttonText.value = "Login"
                    },
                ),
            ) {
                if (openAlertDialog.value) {
                    CustomDialog(
                        openDialogCustom = openAlertDialog,
                        alertDialogTitle.value,
                        alertDialogContent.value,
                        alertDialogFunction.value,
                    )
                }

                if (openLoadingDialog.value) {
                    DialogBoxLoading()
                }

                Box(
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Button(
                        onClick = {
                            viewModel.login(
                                phone = phone,
                                password = password,
                                onLoading = {
                                    openLoadingDialog.value = true
                                },
                                onSuccess = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            BottomNavigationBarActivity::class.java
                                        )
                                    )
                                    activity.finish()
                                    openLoadingDialog.value = false
                                },
                                onError = {
                                    alertDialogTitle.value = "Login Error"
                                    alertDialogContent.value = viewModel.errorMessage!!
                                    openAlertDialog.value = true
                                    openLoadingDialog.value = false
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(scale.value),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        ),
                    ) {
                        Text(
                            text = buttonText.value,
                            color = Color(0xFFFFFFFF),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 6.em
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            state.value = true
                        }
                    }
                }
            }

            @Composable
            fun RenderSubtitleRegister(
                subtitleCurrent: MutableState<String> = remember { mutableStateOf("") },
                subtitleFinal: String = "Not have account yet? Sign up for now!",
                delay: Int = 2500,
            ) {
                Text(
                    text = subtitleCurrent.value,
                    color = greyTextColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 4.em
                    )
                ).let { animation ->
                    LaunchedEffect(animation) {
                        delay(delay.toLong())
                        subtitleFinal.forEachIndexed { index, _ ->
                            delay(20)
                            subtitleCurrent.value = subtitleFinal.substring(0, index + 1)
                        }
                    }
                }
            }

            @Composable
            fun RenderRegisterButton(
                buttonText: MutableState<String> = remember { mutableStateOf("") },
                state: MutableState<Boolean> = remember { mutableStateOf(false) },
                duration: Int = 1000,
                delay: Int = 3000,
                scale: State<Float> = animateFloatAsState(
                    targetValue = if (state.value) 0.8f else 0f,
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    finishedListener = {
                        buttonText.value = "Register"
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    OutlinedButton(
                        onClick = {
                            bottomLoginVisible.value = !bottomLoginVisible.value!!
                            Handler().postDelayed({
                                bottomRegisterVisible.value = !bottomRegisterVisible.value!!
                            }, 750)
                        },
                        border = BorderStroke(1.dp, primaryColor),
                        modifier = Modifier.fillMaxWidth(scale.value),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Text(
                            text = buttonText.value,
                            color = primaryColor,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 6.em
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            state.value = true
                        }
                    }
                }
            }
        }

        inner class RegisterModule {
            private var phone by mutableStateOf("")
            private var password by mutableStateOf("")
            private var reEnterPassword by mutableStateOf("")
            private var name by mutableStateOf("")
            private var dateOfBirth by mutableStateOf("")

            @OptIn(ExperimentalFoundationApi::class)
            @Composable
            fun RenderRegisterForm() {
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
                        .overscroll(overscrollEffect = ScrollableDefaults.overscrollEffect())
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    RenderRegisterTitle()

                    Spacer(modifier = Modifier.height(20.dp))

                    RenderPhoneTextField()

                    Spacer(modifier = Modifier.height(16.dp))

                    RenderPasswordTextField()

                    Spacer(modifier = Modifier.height(16.dp))

                    RenderReEnterPasswordTextField()

                    Spacer(modifier = Modifier.height(16.dp))

                    RenderNameTextField()

                    Spacer(modifier = Modifier.height(16.dp))

                    BirthdayField()

                    Spacer(modifier = Modifier.height(30.dp))

                    RenderRegisterButton()

                    Spacer(modifier = Modifier.height(20.dp))

                    RenderSubtitleLogin()

                    Spacer(modifier = Modifier.height(10.dp))

                    RenderLoginButton()

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            @Composable
            fun RenderRegisterTitle(
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 500,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderPhoneTextField(
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1000,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { phone = it },
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
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderPasswordTextField(
                passwordVisible: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1500,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { password = it },
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
                        visualTransformation = if (passwordVisible.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible.value) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }

                            val description = if (passwordVisible.value) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderReEnterPasswordTextField(
                passwordVisible: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1000,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { reEnterPassword = it },
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
                        visualTransformation = if (passwordVisible.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible.value) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }

                            val description = if (passwordVisible.value) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun RenderNameTextField(
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1500,
            ) {
                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { name = it },
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
                        visible.value = true
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun BirthdayField(
                context: Context = LocalContext.current,
                visible: MutableState<Boolean> = remember { mutableStateOf(false) },
                density: Density = LocalDensity.current,
                duration: Int = 1000,
                delay: Int = 1000,
                interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
            ) {
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

                AnimatedVisibility(
                    visible = visible.value,
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
                        onValueChange = { dateOfBirth = it },
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
                        visible.value = true
                    }
                }
            }

            @Composable
            fun RenderRegisterButton(
                alertDialogFunction: MutableState<() -> Unit> = remember { mutableStateOf({}) },
                openAlertDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                alertDialogTitle: MutableState<String> = remember { mutableStateOf("") },
                alertDialogContent: MutableState<String> = remember { mutableStateOf("") },
                openLoadingDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
                buttonText: MutableState<String> = remember { mutableStateOf("") },
                state: MutableState<Boolean> = remember { mutableStateOf(false) },
                duration: Int = 1000,
                delay: Int = 2000,
                scale: State<Float> = animateFloatAsState(
                    targetValue = if (state.value) 0.8f else 0f,
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    finishedListener = {
                        buttonText.value = "Register"
                    }
                ),
            ) {
                if (openAlertDialog.value) {
                    CustomDialog(
                        openDialogCustom = openAlertDialog,
                        alertDialogTitle.value,
                        alertDialogContent.value,
                        alertDialogFunction.value
                    )
                }

                if (openLoadingDialog.value) {
                    DialogBoxLoading()
                }

                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Button(
                        onClick = {
                            viewModel.register(
                                phone = phone,
                                password = password,
                                reEnterPassword = reEnterPassword,
                                name = name,
                                dateOfBirth = dateOfBirth,
                                onLoading = {
                                    openLoadingDialog.value = true
                                },
                                onSuccess = {
                                    alertDialogTitle.value = "Register Success"
                                    alertDialogContent.value = "Login and enjoy now"
                                    alertDialogFunction.value = {
                                        bottomRegisterVisible.value = !bottomRegisterVisible.value!!
                                        Handler().postDelayed({
                                            bottomLoginVisible.value = !bottomLoginVisible.value!!
                                        }, 750)
                                    }
                                    openAlertDialog.value = true
                                    openLoadingDialog.value = false
                                },
                                onError = {
                                    alertDialogTitle.value = "Register Error"
                                    alertDialogContent.value = viewModel.errorMessage!!
                                    alertDialogFunction.value = {}
                                    openAlertDialog.value = true
                                    openLoadingDialog.value = false
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(scale.value),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        ),
                    ) {
                        Text(
                            text = buttonText.value,
                            color = Color(0xFFFFFFFF),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 6.em
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            state.value = true
                        }
                    }
                }
            }

            @Composable
            fun RenderSubtitleLogin(
                subtitleCurrent: MutableState<String> = remember { mutableStateOf("") },
                subtitleFinal: String = "Already have account? Please sign in.",
                delay: Int = 2500,
            ) {
                Text(
                    text = subtitleCurrent.value,
                    color = greyTextColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 4.em
                    )
                ).let { animation ->
                    LaunchedEffect(animation) {
                        delay(delay.toLong())
                        subtitleFinal.forEachIndexed { index, _ ->
                            delay(20)
                            subtitleCurrent.value = subtitleFinal.substring(0, index + 1)
                        }
                    }
                }
            }

            @Composable
            fun RenderLoginButton(
                buttonText: MutableState<String> = remember { mutableStateOf("") },
                state: MutableState<Boolean> = remember { mutableStateOf(false) },
                duration: Int = 1000,
                delay: Int = 3000,
                scale: State<Float> = animateFloatAsState(
                    targetValue = if (state.value) 0.8f else 0f,
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    finishedListener = {
                        buttonText.value = "Login"
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    OutlinedButton(
                        onClick = {
                            bottomRegisterVisible.value = !bottomRegisterVisible.value!!
                            Handler().postDelayed({
                                bottomLoginVisible.value = !bottomLoginVisible.value!!
                            }, 750)
                        },
                        border = BorderStroke(1.dp, primaryColor),
                        modifier = Modifier.fillMaxWidth(scale.value),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Text(
                            text = buttonText.value,
                            color = primaryColor,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 6.em
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            state.value = true
                        }
                    }
                }
            }
        }
    }
}