package com.example.kotlinjetpack.view.auth

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import com.example.kotlinjetpack.function.shadow
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view.auth.login_packages.LoginForm
import com.example.kotlinjetpack.view.auth.register_packages.RegisterForm

class LoginAndRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
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

    class TopLayout {
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

    class BottomLayout {
        @Composable
        fun RenderBottomLayout(
            context: Context = LocalContext.current,
            activity: Activity = ((context as? Activity)!!),
            lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
            density: Density = LocalDensity.current,
            modifier: Modifier,
            bottomLoginVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
            bottomRegisterVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
            duration: Int = 1500,
            delay: Int = 0
        ) {
            Column(
                modifier = modifier,
            ) {
                AnimatedVisibility(
                    visible = bottomLoginVisible.value,
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
                            activity = activity,
                            onRegisterClicked = {
                                bottomLoginVisible.value = !bottomLoginVisible.value
                                Handler().postDelayed( {
                                    bottomRegisterVisible.value = !bottomRegisterVisible.value
                                }, 1500)
                            }
                        )
                    }
                }.let { animation ->
                    LaunchedEffect(animation) {
                        bottomLoginVisible.value = !bottomLoginVisible.value
                    }
                }

                AnimatedVisibility(
                    visible = bottomRegisterVisible.value,
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
                                bottomRegisterVisible.value = !bottomRegisterVisible.value
                                Handler().postDelayed( {
                                    bottomLoginVisible.value = !bottomLoginVisible.value
                                }, 1500)
                            },
                            onLoginClicked = {
                                bottomRegisterVisible.value = !bottomRegisterVisible.value
                                Handler().postDelayed( {
                                    bottomLoginVisible.value = !bottomLoginVisible.value
                                }, 1500)
                            },
                        )
                    }
                }
            }
        }
    }
}




