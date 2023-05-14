package com.example.kotlinjetpack.view.auth

import android.app.Activity
import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.os.Handler
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinjetpack.function.shadow
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view.auth.login_packages.LoginForm
import com.example.kotlinjetpack.view.auth.register_packages.RegisterForm

class LoginAndRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Preview
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current

    var topVisible by remember { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            topVisible = !isKeyboardOpen
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

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
        val density = LocalDensity.current

        Column(
            modifier = Modifier.animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val duration = 500
            val delay = 0
            AnimatedVisibility(
                visible = topVisible,
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
                    MessageText()
                }
            }
        }.let { animation ->
            LaunchedEffect(animation) {
                topVisible = true
            }
        }

        var bottomLoginVisible by remember { mutableStateOf(false) }
        var bottomRegisterVisible by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            val duration = 1500
            val delay = 0

            AnimatedVisibility(
                visible = bottomLoginVisible,
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
                )  + shrinkVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ) + fadeOut(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ),
                modifier = Modifier
                    .weight(1f),
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
                    LoginForm(
                        lifecycleOwner = lifecycleOwner,
                        context = context,
                        activity = activity!!,
                        onRegisterClicked = {
                            bottomLoginVisible = !bottomLoginVisible
                            Handler().postDelayed( {
                                bottomRegisterVisible = !bottomRegisterVisible
                            }, 1500)
                        }
                    )
                }
            }.let { animation ->
                LaunchedEffect(animation) {
                    bottomLoginVisible = !bottomLoginVisible
                }
            }

            AnimatedVisibility(
                visible = bottomRegisterVisible,
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
                )  + shrinkVertically(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ) + fadeOut(
                    animationSpec = tween(durationMillis = duration, delayMillis = delay),
                ),
                modifier = Modifier.weight(1f),
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
                    RegisterForm(
                        lifecycleOwner = lifecycleOwner,
                        context = context,
                        onAlertDialogClick = {
                            bottomRegisterVisible = !bottomRegisterVisible
                            Handler().postDelayed( {
                                bottomLoginVisible = !bottomLoginVisible
                            }, 1500)
                        },
                        onLoginClicked = {
                            bottomRegisterVisible = !bottomRegisterVisible
                            Handler().postDelayed( {
                                bottomLoginVisible = !bottomLoginVisible
                            }, 1500)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MessageText() {
    var textState by remember { mutableStateOf(false) }
    val duration = 1000
    val delay = 0
    val scale: Float by animateFloatAsState(
        targetValue = if (!textState) 0f else 50f,
        animationSpec = tween(durationMillis = duration, delayMillis = delay),
    )

    Text(
        text = "Messaging App",
        color = Color(0xFFFFFFFF),
        style = TextStyle(
            fontSize = scale.sp
        )
    ).let { animation ->
        LaunchedEffect(animation) {
            textState = true
        }
    }
}


