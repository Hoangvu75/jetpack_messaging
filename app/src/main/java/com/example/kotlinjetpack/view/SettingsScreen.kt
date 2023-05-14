package com.example.kotlinjetpack.view

import android.os.Process.killProcess
import android.os.Process.myPid
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun SettingsScreen() {
    val topR by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 123,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topG by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 123,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topB by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 255,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topA by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.3).toInt(),
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )

    val botR by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 255,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botG by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 87,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botB by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 31 else 199,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botA by animateIntAsState(
        targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.7).toInt(),
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val toolbarState = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(topR, topG, topB, topA),
                        Color(botR, botG, botB, botA),
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                ),
            ),
        state = toolbarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (AppSettings.isDarkMode) {
                                listOf(
                                    softPink.copy(1f, 0.2f, 0.1f, 0.2f),
                                    primaryColor.copy(1f, 0.4f, 0f, 0.2f),
                                )
                            } else {
                                listOf(
                                    softPink,
                                    primaryColor
                                )
                            },
                        ),
                        shape = RoundedCornerShape(
                            bottomEnd = 150.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            bottomEnd = 150.dp
                        )
                    ),
                contentAlignment = Alignment.BottomStart
            ) {
                var title by remember { mutableStateOf("") }
                val titleFinal = "Settings"

                Text(
                    text = title,
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                    fontSize = 35.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                ).let { animation ->
                    LaunchedEffect(animation) {
                        delay(500)
                        titleFinal.forEachIndexed { index, _ ->
                            delay(20)
                            title = titleFinal.substring(0, index + 1)
                        }
                    }
                }
            }
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CloseAppButton()
        }
    }
}

@Composable
fun CloseAppButton(
    density: Density = LocalDensity.current
) {
    var visible by remember { mutableStateOf(false) }
    val duration = 2500
    val delay = 500

    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.8f else 1f,
        tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )
    val initialRotationAngle by animateFloatAsState(
        targetValue = if (visible) 3600f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing),
    )

    val infiniteOnPressRotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = if (buttonState == ButtonState.Pressed) 10000f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000),
            repeatMode = RepeatMode.Restart
        )
    )

    val infiniteScale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        )
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = duration,
                delayMillis = delay
            ),
        ) {
            with(density) { 500.dp.roundToPx() }
        } + fadeIn(
            animationSpec = tween(
                durationMillis = duration,
                delayMillis = delay
            ),
            initialAlpha = 0f
        ),
        modifier = Modifier
            .graphicsLayer {
                rotationY = initialRotationAngle
            }
            .offset(0.dp, (-100).dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .graphicsLayer(
                    rotationX = infiniteOnPressRotation,
                    rotationY = infiniteOnPressRotation,
                    rotationZ = infiniteOnPressRotation,
                    scaleX = infiniteScale,
                    scaleY = infiniteScale
                )
                .scale(scale)
                .padding(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    killProcess(myPid())
                }
                .background(
                    color = primaryColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
                .pointerInput(buttonState) {
                    awaitPointerEventScope {
                        buttonState = if (buttonState == ButtonState.Pressed) {
                            waitForUpOrCancellation()
                            ButtonState.Idle
                        } else {
                            awaitFirstDown(false)
                            ButtonState.Pressed
                        }
                    }
                },
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = "Close app",
                color = Color.White,
                fontSize = 30.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }.let { animation ->
        LaunchedEffect(animation) {
            visible = true
        }
    }
}
