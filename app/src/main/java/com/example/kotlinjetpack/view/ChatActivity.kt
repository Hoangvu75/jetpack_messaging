package com.example.kotlinjetpack.view

import AddChatRequestBody
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinjetpack.const.BASE_URL
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.model.Chat
import com.example.kotlinjetpack.model.User
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import com.example.kotlinjetpack.view_model.AddChatViewModel
import com.example.kotlinjetpack.view_model.GetChatViewModel
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.util.*

class ChatActivity : ComponentActivity() {
    private var mSocket: Socket? = null
    private var chatList = mutableStateListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.getStringExtra("chatId")
        val phone = intent.getStringExtra("phone")
        try {
            mSocket = IO.socket(BASE_URL)
            mSocket!!.connect()

            mSocket!!.on("chat response") { args ->
                val data = args[0] as JSONObject
                if (data.getString("receiver") == PHONE) {
                    val newChat = Chat(
                        sender = phone,
                        content = data.getString("message"),
                        time = System.currentTimeMillis(),
                        _id = null,
                    )
                    chatList.add(newChat)
                }
                println("response: ")
            }
            setContent {
                ChatScreen(chatId = chatId!!, socket = mSocket!!, chatList)
            }
        } catch (_: Error) {
            println("Socket error")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, socket: Socket, chatList: MutableList<Chat>) {
    println("socket: ${socket.connected()}")

    val lifecycleOwner = LocalLifecycleOwner.current
    var message by remember { mutableStateOf("") }

    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var user1 by remember {
        mutableStateOf(
            User(
                null, null, null
            )
        )
    }
    var user2 by remember {
        mutableStateOf(
            User(
                null, null, null
            )
        )
    }

    val getChatViewModel = GetChatViewModel()

    if (chatList.isEmpty()) {
        getChatViewModel.getChat(chatId)
        getChatViewModel.resultLiveData.observe(lifecycleOwner) { result ->
            when (result) {
                0 -> {
                    println("Loading")
                }

                1 -> {
                    println("Success, chat: ${getChatViewModel.getChatData!!.chat}")
                    chatList.clear()
                    chatList.addAll(getChatViewModel.getChatData!!.chat!!.chat!!)

                    if (getChatViewModel.getChatData!!.chat!!.users!![0].phone == PHONE) {
                        phone = getChatViewModel.getChatData!!.chat!!.users!![1].phone!!
                        name = getChatViewModel.getChatData!!.chat!!.users!![1].name!!
                    } else {
                        phone = getChatViewModel.getChatData!!.chat!!.users!![0].phone!!
                        name = getChatViewModel.getChatData!!.chat!!.users!![0].name!!
                    }

                    user1 = getChatViewModel.getChatData!!.chat!!.users!![0]
                    user2 = getChatViewModel.getChatData!!.chat!!.users!![1]
                }

                2 -> {
                    println("Error: ${getChatViewModel.errorMessage}")
                }
            }
        }
    }

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

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(topR, topG, topB, topA),
                            Color(botR, botG, botB, botA),
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
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
                                bottomStart = 20.dp
                            )
                        )
                        .padding(10.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 20.dp
                            )
                        ),
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
                            .size(45.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (name.isEmpty()) "" else name[0].uppercase(),
                            style = TextStyle(
                                fontSize = 7.em,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column() {
                        Text(
                            text = name,
                            modifier = Modifier.fillMaxWidth(1f),
                            style = MaterialTheme.typography.titleLarge.copy(
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                fontSize = 5.em
                            ),
                        )
                        Text(
                            text = phone,
                            modifier = Modifier.fillMaxWidth(1f),
                            style = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Start,
                                color = Color.White.copy(alpha = 0.7f),
                                fontStyle = FontStyle(1),
                                fontSize = 4.em
                            ),
                        )
                    }

                }

                val listState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .verticalScroll(listState)
                ) {
                    for (i in 0 until chatList.size) {
                        ChatElement(
                            sender = chatList[i].sender!!,
                            content = chatList[i].content!!,
                        )
                    }

                    LaunchedEffect(chatList.size) {
                        listState.scrollTo(Int.MAX_VALUE)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
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
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = defaultTextColor(),
                        fontSize = 4.em
                    )
                )

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        val addChatViewModel = AddChatViewModel()
                        val addChatRequestBody = AddChatRequestBody(
                            user_1 = user1,
                            user_2 = user2,
                            sender = PHONE,
                            content = message
                        )
                        chatList.add(
                            Chat(
                                sender = PHONE,
                                content = message,
                                time = System.currentTimeMillis(),
                                _id = null,
                            )
                        )
                        socket.emit("chat request", JSONObject().apply {
                            put("message", message)
                            put("receiver", phone)
                            println("request")
                        })
                        addChatViewModel.addChat(addChatRequestBody)
                        message = ""
                    }
                ) {
                    Icon(
                        Icons.Filled.Send,
                        modifier = Modifier.size(50.dp),
                        contentDescription = null,
                        tint = primaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun ChatElement(
    sender: String,
    content: String
) {
    var state by remember { mutableStateOf(false) }
    val duration = 1000

    val scale: Float by animateFloatAsState(
        targetValue = if (state) 1f else 0f,
        animationSpec = tween(durationMillis = duration),
    )

    if (sender == PHONE) {
        Row {
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = primaryColor,
                modifier = Modifier
                    .padding(top = 5.dp, start = 100.dp, end = 10.dp, bottom = 5.dp)
                    .scale(scale)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(all = 10.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        Color.White,
                        fontSize = 4.em
                    )
                )
            }
        }.let { animation ->
            LaunchedEffect(animation) {
                state = true
            }
        }
    } else {
        Row {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = greyTextColor,
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp, end = 100.dp, bottom = 5.dp)
                    .scale(scale)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(all = 10.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(Color.White)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }.let { animation ->
            LaunchedEffect(animation) {
                state = true
            }
        }
    }
}

