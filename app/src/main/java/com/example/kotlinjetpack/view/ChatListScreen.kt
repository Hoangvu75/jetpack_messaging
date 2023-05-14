package com.example.kotlinjetpack.view

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.function.AppSettings.getChatListViewModel
import com.example.kotlinjetpack.function.AppSettings.isDarkMode
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ChatListScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current
) {
    AndroidThreeTen.init(context)

    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                if (getChatListViewModel.chatItemList.isEmpty()) {
                    getChatListViewModel.getChatList()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                getChatListViewModel.clearChatList()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val topR by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 123,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topG by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 123,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topB by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 255,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val topA by animateIntAsState(
        targetValue = if (isDarkMode) 255 else (255 * 0.3).toInt(),
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )

    val botR by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 255,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botG by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 87,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botB by animateIntAsState(
        targetValue = if (isDarkMode) 31 else 199,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    val botA by animateIntAsState(
        targetValue = if (isDarkMode) 255 else (255 * 0.7).toInt(),
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
            val textSize = (20 + (30 - 15) * toolbarState.toolbarState.progress).sp

            Row(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isDarkMode) {
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
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(175.dp)
                    .pin(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (toolbarState.toolbarState.progress == 1f) {
                    ScreenModeButton()

                    DateTimeText()
                }
            }

            var title by remember { mutableStateOf("") }
            val titleFinal = "Chat with friends"
            Text(
                text = title,
                modifier = Modifier
                    .road(Alignment.TopCenter, Alignment.BottomStart)
                    .padding(20.dp),
                color = Color.White,
                fontSize = textSize,
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
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Recent contacts",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(all = 20.dp),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    color = defaultTextColor(),
                    fontSize = 28.sp
                ),
            )


            for (i in 0 until getChatListViewModel.chatItemList.size) {
                val density = LocalDensity.current
                var chatListVisible by remember { mutableStateOf(false) }
                val chatListDuration = 500
                val chatListDelay = 0 + 100 * i
                AnimatedVisibility(
                    visible = chatListVisible,
                    enter = slideInVertically(
                        animationSpec = tween(
                            durationMillis = chatListDuration,
                            delayMillis = chatListDelay
                        ),
                    ) {
                        with(density) {
                            -50.dp.roundToPx()
                        }
                    } + fadeIn(
                        animationSpec = tween(
                            durationMillis = chatListDuration,
                            delayMillis = chatListDelay
                        ),
                        initialAlpha = 0f
                    )
                ) {
                    ChatItem(
                        nameList = listOf(
                            getChatListViewModel.chatItemList[i].users!![0].name!!,
                            getChatListViewModel.chatItemList[i].users!![1].name!!
                        ),
                        phoneList = listOf(
                            getChatListViewModel.chatItemList[i].users!![0].phone!!,
                            getChatListViewModel.chatItemList[i].users!![1].phone!!
                        ),
                        lastMessageContent =
                        if (getChatListViewModel.chatItemList[i].chat!!.isNotEmpty())
                            getChatListViewModel.chatItemList[i].chat!!.last().content!!
                        else "",
                        lastMessageTime =
                        if (getChatListViewModel.chatItemList[i].chat!!.isNotEmpty())
                            SimpleDateFormat("dd/MM  hh:mm").format(
                                getChatListViewModel.chatItemList[i].chat!!.last().time!!
                            )
                        else "",
                        chatId = getChatListViewModel.chatItemList[i]._id!!
                    )
                }.let { animation ->
                    LaunchedEffect(animation) {
                        chatListVisible = true
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    LaunchedEffect(toolbarState.toolbarState.progress) {
        Log.d("CollapsingToolbar", "Progress: ${toolbarState.toolbarState.progress}")
    }
}


@Composable
fun ChatItem(
    nameList: List<String>,
    phoneList: List<String>,
    lastMessageContent: String,
    lastMessageTime: String,
    chatId: String,
) {
    val context = LocalContext.current

    val friendName: String
    val friendPhone: String
    if (phoneList[0] == PHONE) {
        friendName = nameList[1]
        friendPhone = phoneList[1]
    } else {
        friendName = nameList[0]
        friendPhone = phoneList[0]
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("chatId", chatId)
                intent.putExtra("phone", friendPhone)
                context.startActivity(intent)
            }
    ) {
        Divider(
            color = greyTextColor,
            thickness = 1.dp,
        )

        Row(
            modifier = Modifier.padding(
                all = 16.dp
            ),
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
                    text = friendName[0].toString().uppercase(),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Row {
                    Text(
                        text = friendName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Start,
                            color = defaultTextColor(),
                        ),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = lastMessageTime,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Start,
                            color = greyTextColor,
                        ),
                    )
                }
                if (lastMessageContent.isEmpty()) {
                    Text(
                        text = "You and this person have no conversation yet",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Start,
                            color = greyTextColor,
                            fontStyle = FontStyle.Italic
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = lastMessageContent,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Start,
                            color = greyTextColor,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ObsoleteCoroutinesApi::class)
@Composable
fun DateTimeText() {
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDate)

    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(true) {
        val ticker = ticker(delayMillis = 1000)
        for (event in ticker) {
            currentTime = LocalTime.now()
        }
    }

    val density = LocalDensity.current

    Column(
        modifier = Modifier.padding(
            all = 20.dp
        ),
        horizontalAlignment = Alignment.End
    ) {
        var dateVisible by remember { mutableStateOf(false) }
        val dateDuration = 1000
        val dateDelay = 600

        AnimatedVisibility(
            visible = dateVisible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = dateDuration, delayMillis = dateDelay),
            ) {
                with(density) { -150.dp.roundToPx() }
            } + fadeIn(
                animationSpec = tween(durationMillis = dateDuration, delayMillis = dateDelay),
                initialAlpha = 0f
            ),
        ) {
            Text(
                text = formattedDate,
                color = Color.White,
                fontSize = 18.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
            )
        }.let { animation ->
            LaunchedEffect(animation) {
                dateVisible = true
            }
        }

        var timeVisible by remember { mutableStateOf(false) }
        val timeDuration = 1000
        val timeDelay = 900

        AnimatedVisibility(
            visible = timeVisible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = timeDuration, delayMillis = timeDelay),
            ) {
                with(density) { 150.dp.roundToPx() }
            } + fadeIn(
                animationSpec = tween(durationMillis = timeDuration, delayMillis = timeDelay),
                initialAlpha = 0f
            )
        ) {
            Text(
                text = currentTime.format(
                    DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                    )
                ),
                color = Color.White,
                fontSize = 16.sp,
                style = TextStyle(
                    fontStyle = FontStyle.Italic
                )
            )
        }.let { animation ->
            LaunchedEffect(animation) {
                timeVisible = true
            }
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
                    isDarkMode = !isDarkMode
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
                    id = if (isDarkMode) R.drawable.ic_baseline_light_mode_24 else R.drawable.ic_baseline_dark_mode_24
                ),
                contentDescription = "mode",
                tint = Color.White,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotationAngle
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (isDarkMode) "Light" else "Dark",
                color = Color.White,
                fontSize = 20.sp,
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