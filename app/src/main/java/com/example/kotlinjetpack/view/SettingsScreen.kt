package com.example.kotlinjetpack.view

import android.os.Process.killProcess
import android.os.Process.myPid
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Password
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.util.Random

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
            Column(
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
                    )
            ) {
                var title by remember { mutableStateOf("") }
                val titleFinal = "Settings"

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    ScreenModeButton()
                }


                Text(
                    text = title,
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                    fontSize = 8.em,
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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var panelState by remember { mutableStateOf(false) }
            val duration = 1000
            val delay = 0
            val scale: Float by animateFloatAsState(
                targetValue = if (panelState) 1f else 0f,
                animationSpec = tween(durationMillis = duration, delayMillis = delay),
            )

            Row(
                modifier = Modifier
                    .scale(scale)
                    .padding(20.dp)
                    .fillMaxWidth(1f)
                    .background(
                        if (AppSettings.isDarkMode) Color(0xBF474747) else Color(0x80FFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(
                                Random().nextInt(256),
                                Random().nextInt(256),
                                Random().nextInt(256),
                            ).copy(
                                alpha = 1f
                            ),
                            shape = CircleShape
                        )
                        .size(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = NAME[0].toString().uppercase(),
                        style = TextStyle(
                            fontSize = 7.em,
                            fontWeight = FontWeight.Bold,
                            color = defaultTextColor()
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column() {
                    Text(
                        text = NAME,
                        style = TextStyle(
                            fontSize = 6.em,
                            fontWeight = FontWeight.Bold,
                            color = defaultTextColor()
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = PHONE,
                        style = TextStyle(
                            fontSize = 5.em,
                            fontStyle = FontStyle.Italic,
                            color = greyTextColor
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }.let { animation ->
                LaunchedEffect(animation) {
                    panelState = true
                }
            }


            Column(
                modifier = Modifier
                    .scale(scale)
                    .padding(20.dp)
                    .fillMaxWidth(1f)
                    .background(
                        if (AppSettings.isDarkMode) Color(0xBF474747) else Color(0x80FFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        )
                ) {
                    Text(
                        text = "Change password",
                        style = TextStyle(
                            fontSize = 5.em,
                            color = defaultTextColor()
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Filled.Password,
                        contentDescription = "icon-password",
                        tint = defaultTextColor()
                    )
                }

                Divider(
                    modifier = Modifier
                        .background(
                            greyTextColor
                        )
                )

                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        )
                ) {
                    Text(
                        text = "Change name",
                        style = TextStyle(
                            fontSize = 5.em,
                            color = defaultTextColor()
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "icon-account",
                        tint = defaultTextColor()
                    )
                }

                Divider(
                    modifier = Modifier
                        .background(
                            greyTextColor
                        )
                )

                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 10.dp
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            killProcess(myPid())
                        }
                ) {
                    Text(
                        text = "Close",
                        style = TextStyle(
                            fontSize = 5.em,
                            color = defaultTextColor()
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "icon-close",
                        tint = defaultTextColor()
                    )
                }
            }
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

@Composable
fun ScreenModeButton() {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.8f else 1f,
        tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )

    var visible by remember { mutableStateOf(false) }
    val duration = 1000
    val delay = 300
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = duration, delayMillis = delay),
        ) {
            with(density) { -150.dp.roundToPx() }
        } + fadeIn(
            animationSpec = tween(durationMillis = duration, delayMillis = delay),
            initialAlpha = 0f
        )
    ) {
        Row(
            modifier = Modifier
                .scale(scale)
                .padding(all = 20.dp)
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    AppSettings.isDarkMode = !AppSettings.isDarkMode
                }
                .padding(all = 10.dp)
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
        ) {
            val rotationAngle by animateFloatAsState(
                targetValue = if (buttonState == ButtonState.Pressed) 720f else 0f,
                animationSpec = tween(durationMillis = 720, easing = LinearOutSlowInEasing),
            )

            val scaleText by animateFloatAsState(
                targetValue = if (buttonState == ButtonState.Pressed) 0f else 1f,
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            )

            Icon(
                painterResource(
                    id = if (AppSettings.isDarkMode) R.drawable.ic_baseline_light_mode_24 else R.drawable.ic_baseline_dark_mode_24
                ),
                contentDescription = "mode",
                tint = Color.White,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotationAngle
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (AppSettings.isDarkMode) "Light" else "Dark",
                color = Color.White,
                fontSize = 5.em,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.scale(scaleText)
            )
        }
    }.let { animation ->
        LaunchedEffect(animation) {
            visible = true
        }
    }


}
