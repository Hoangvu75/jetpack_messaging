@file:Suppress("DEPRECATION")

package com.example.kotlinjetpack.view

import AddChatRequestBody
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.VideoCall
import androidx.compose.material.icons.rounded.VideoChat
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlinjetpack.const.BASE_URL
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.function.over_scroll.overScrollVertical
import com.example.kotlinjetpack.function.over_scroll.parabolaScrollEasing
import com.example.kotlinjetpack.model.Chat
import com.example.kotlinjetpack.model.User
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import com.example.kotlinjetpack.view.video_call_activity.MeetingActivity
import com.example.kotlinjetpack.view_model.AddChatViewModel
import com.example.kotlinjetpack.view_model.GetChatViewModel
import com.example.kotlinjetpack.view_model.JoinViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.util.Locale
import java.util.Random


class ChatActivity : ComponentActivity() {
    private var mSocket: Socket? = null
    private var chatList = mutableStateListOf<Chat>()
    private lateinit var tts: TextToSpeech
    private val joinViewModel: JoinViewModel = JoinViewModel()
    private var authToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJjYmVhNTM2Yy02OTEyLTRmMzItYTg3Yy02YjdjYTU2MWIxZmQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY4NjQ2Mjk4OSwiZXhwIjoxODQ0MjUwOTg5fQ.OIi2_RZZUQ4sG53Z0I5JXABNmLeUWjtcgz0DVjOHHfg"

    companion object {
        private const val PERMISSION_REQ_ID = 22
        private val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
                                style = MaterialTheme.typography.titleLarge.copy(
                                    textAlign = TextAlign.Start,
                                    color = Color.White,
                                    fontSize = 5.em
                                ),
                            )
                            Text(
                                text = phone,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textAlign = TextAlign.Start,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontStyle = FontStyle(1),
                                    fontSize = 4.em
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            Icons.Rounded.VideoCall,
                            contentDescription = "vid_call_icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    joinViewModel.createRoom(
                                        authToken = authToken,
                                        onLoading = {
                                            Toast
                                                .makeText(
                                                    this@ChatActivity,
                                                    "Creating video chat room...",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },
                                        onSuccess = {
                                            val meetingId = joinViewModel.roomId
                                            val intent = Intent(
                                                this@ChatActivity,
                                                MeetingActivity::class.java
                                            )

                                            val meetingRequestString =
                                                "videosdk_meetingId: $meetingId"
                                            val addChatViewModel = AddChatViewModel()
                                            val addChatRequestBody = AddChatRequestBody(
                                                user_1 = user1,
                                                user_2 = user2,
                                                sender = PHONE,
                                                content = meetingRequestString
                                            )
                                            chatList.add(
                                                Chat(
                                                    sender = PHONE,
                                                    content = meetingRequestString,
                                                    time = System.currentTimeMillis(),
                                                    _id = null,
                                                )
                                            )
                                            socket.emit("chat request", JSONObject().apply {
                                                put("message", meetingRequestString)
                                                put("receiver", phone)
                                                println("request")
                                            })
                                            addChatViewModel.addChat(addChatRequestBody)

                                            intent.putExtra("token", authToken)
                                            intent.putExtra("meetingId", meetingId)
                                            startActivity(intent)
                                        },
                                        onError = {
                                            Toast
                                                .makeText(
                                                    this@ChatActivity,
                                                    "Some error occured! Please try again!",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    )
                                }
                        )
                    }

                    val listState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .overScrollVertical(
                                true,
                                { x1, x2 -> parabolaScrollEasing(x1, x2, 5f) },
                                springStiff = 300f,
                                springDamp = 0.3f,
                            )
                            .verticalScroll(listState)
                    ) {
                        for (i in 0 until chatList.size) {
                            ChatElement(
                                sender = chatList[i].sender!!,
                                content = chatList[i].content!!,
                            )
                        }
                    }.let {
                        LaunchedEffect(chatList.size) {
                            listState.animateScrollTo(
                                value = Int.MAX_VALUE,
                                animationSpec = tween(durationMillis = 750, delayMillis = 0)
                            )
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

                    Box(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (message.isBlank()) {
                                Toast.makeText(
                                    this@ChatActivity,
                                    "Please type something",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
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
                        }
                    ) {
                        Icon(
                            Icons.Filled.Send,
                            modifier = Modifier
                                .size(45.dp)
                                .padding(5.dp),
                            contentDescription = null,
                            tint = primaryColor,
                        )
                    }
                }
            }
        }
    }

    private fun ttsSpeak(text: String, locale: String, context: Context) {
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.forLanguageTag(locale)
                Toast.makeText(context, "tts speak", Toast.LENGTH_SHORT).show()
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
        tts.stop()
    }

    @Composable
    fun ChatElement(
        sender: String,
        content: String,
        context: Context = LocalContext.current,
        density: Density = LocalDensity.current
    ) {
        var state by remember { mutableStateOf(false) }
        val duration = 1000
        val scale: Float by animateFloatAsState(
            label = "FloatAnimation",
            targetValue = if (state) 1f else 0f,
            animationSpec = tween(durationMillis = duration),
        )

        val languageIdentifier = LanguageIdentification.getClient()

        var languageCode by remember { mutableStateOf("und") }
        languageIdentifier.identifyLanguage(content)
            .addOnSuccessListener {
                languageCode = if (it.length > 2) {
                    "und"
                } else {
                    it
                }
            }.addOnFailureListener {
                languageCode = "und"
            }

        var translateText by remember { mutableStateOf("") }

        var translateLoading by remember { mutableStateOf(false) }

        var messageTextWidth by remember {
            mutableStateOf(0.dp)
        }

        fun translate() {
            translateLoading = true

            val options = TranslatorOptions
                .Builder()
                .setSourceLanguage(languageCode)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
            val translator = Translation.getClient(options)
            val conditions = DownloadConditions
                .Builder()
                .requireWifi()
                .build()

            translator
                .downloadModelIfNeeded(conditions)
                .addOnFailureListener {
                    translateLoading = false
                    languageCode = "und"
                    Toast.makeText(
                        context, "Can't download language kit",
                        Toast.LENGTH_SHORT,
                    ).show()
                }.addOnSuccessListener {}.addOnCompleteListener {
                    translator
                        .translate(content)
                        .addOnSuccessListener {
                            translateText = it
                        }.addOnFailureListener {
                            Toast.makeText(
                                context, "Can't translate language",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }.addOnCompleteListener {
                            translator.close()
                            translateLoading = false
                        }
                }

        }

        if (sender == PHONE) {
            if (content.contains("videosdk_meetingId: ")) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = primaryColor,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 50.dp, end = 50.dp, bottom = 5.dp)
                        .scale(scale)
                ) {
                    Column {
                        Text(
                            text = "Meeting room",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                Color.White,
                                fontSize = 5.em,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    vertical = 5.dp
                                )
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                        Column(
                            modifier = Modifier
                                .padding(all = 20.dp)
                                .onGloballyPositioned {
                                    messageTextWidth = with(density) { it.size.width.toDp() }
                                }
                                .animateContentSize(
                                    animationSpec = tween(durationMillis = 750, delayMillis = 0)
                                ),
                        ) {
                            Text(
                                text = "You created a meeting room",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    Color.White,
                                    fontSize = 4.em,
                                    fontStyle = FontStyle.Italic,
                                ),
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Room id: ${content.replace("videosdk_meetingId: ", "")}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    Color.White,
                                    fontSize = 4.em,
                                    fontStyle = FontStyle.Italic,
                                ),
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            var buttonState by remember { mutableStateOf(ButtonState.Idle) }
                            val buttonScale by animateFloatAsState(
                                if (buttonState == ButtonState.Pressed) 0.8f else 1f,
                                tween(durationMillis = 150, easing = FastOutSlowInEasing),
                                label = "scaleButtonAnim"
                            )

                            Box(
                                modifier = Modifier
                                    .scale(buttonScale)
                                    .fillMaxWidth(1f)
                                    .background(
                                        color = Color.White.copy(alpha = 0.2F),
                                        shape = RoundedCornerShape(20)
                                    )
                                    .padding(10.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        val meetingId = content.replace("videosdk_meetingId: ", "")
                                        val intent =
                                            Intent(this@ChatActivity, MeetingActivity::class.java)
                                        intent.putExtra("token", authToken)
                                        intent.putExtra("meetingId", meetingId)
                                        startActivity(intent)
                                    }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth(1f),
                                ) {
                                    Text(
                                        text = "Click to join",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            Color.White,
                                            fontSize = 4.em,
                                            fontWeight = FontWeight.Bold
                                        ),
                                    )

                                    Spacer(modifier = Modifier.width(20.dp))

                                    Icon(
                                        Icons.Rounded.VideoChat,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                            }


                        }
                    }
                }.let { animation ->
                    LaunchedEffect(animation) {
                        state = true
                    }
                }
            } else {
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
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            state = true
                        }
                    }
                }
            }
        } else {
            if (content.contains("videosdk_meetingId: ")) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = greyTextColor,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 50.dp, end = 50.dp, bottom = 5.dp)
                        .scale(scale)
                ) {
                    Column {
                        Text(
                            text = "Meeting room",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                Color.White,
                                fontSize = 5.em,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    vertical = 5.dp
                                )
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                        Column(
                            modifier = Modifier
                                .padding(all = 20.dp)
                                .onGloballyPositioned {
                                    messageTextWidth = with(density) { it.size.width.toDp() }
                                }
                                .animateContentSize(
                                    animationSpec = tween(durationMillis = 750, delayMillis = 0)
                                ),
                        ) {
                            Text(
                                text = "Your friend created a meeting room",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    Color.White,
                                    fontSize = 4.em,
                                    fontStyle = FontStyle.Italic,
                                ),
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Room id: ${content.replace("videosdk_meetingId: ", "")}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    Color.White,
                                    fontSize = 4.em,
                                    fontStyle = FontStyle.Italic,
                                ),
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            var buttonState by remember { mutableStateOf(ButtonState.Idle) }
                            val buttonScale by animateFloatAsState(
                                if (buttonState == ButtonState.Pressed) 0.8f else 1f,
                                tween(durationMillis = 150, easing = FastOutSlowInEasing),
                                label = "scaleButtonAnim"
                            )

                            Box(
                                modifier = Modifier
                                    .scale(buttonScale)
                                    .fillMaxWidth(1f)
                                    .background(
                                        color = Color.White.copy(alpha = 0.2F),
                                        shape = RoundedCornerShape(20)
                                    )
                                    .padding(10.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        val meetingId = content.replace("videosdk_meetingId: ", "")
                                        val intent =
                                            Intent(this@ChatActivity, MeetingActivity::class.java)
                                        intent.putExtra("token", authToken)
                                        intent.putExtra("meetingId", meetingId)
                                        startActivity(intent)
                                    }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth(1f),
                                ) {
                                    Text(
                                        text = "Click to join",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            Color.White,
                                            fontSize = 4.em,
                                            fontWeight = FontWeight.Bold
                                        ),
                                    )

                                    Spacer(modifier = Modifier.width(20.dp))

                                    Icon(
                                        Icons.Rounded.VideoChat,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                            }


                        }
                    }

                }.let { animation ->
                    LaunchedEffect(animation) {
                        state = true
                    }
                }
            } else {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = greyTextColor,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 10.dp, end = 100.dp, bottom = 5.dp)
                        .scale(scale)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .onGloballyPositioned {
                                messageTextWidth = with(density) { it.size.width.toDp() }
                            }
                            .animateContentSize(
                                animationSpec = tween(durationMillis = 750, delayMillis = 0)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    Color.White,
                                    fontSize = 4.em
                                ),
                                modifier = Modifier.weight(1f, false)
                            )

                            if (languageCode != "und") {
                                Spacer(modifier = Modifier.width(10.dp))

                                Icon(
                                    Icons.Rounded.Mic,
                                    contentDescription = "speaker_icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            ttsSpeak(
                                                text = content,
                                                locale = languageCode,
                                                context = context
                                            )
                                        }
                                )
                            }
                        }

                        if (translateText.isNotBlank()) {
                            Divider(
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .width(messageTextWidth)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = translateText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        Color.White,
                                        fontSize = 4.em,
                                        fontStyle = FontStyle.Italic
                                    ),
                                    modifier = Modifier.weight(1f, false)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Icon(
                                    Icons.Rounded.Mic,
                                    contentDescription = "speaker_icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            ttsSpeak(
                                                text = translateText,
                                                locale = "en",
                                                context = context
                                            )
                                        }
                                )
                            }

                        }

                        if (
                            languageCode != TranslateLanguage.ENGLISH
                            && languageCode != "und"
                            && translateText.isBlank()
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        translate()
                                    }
                            ) {
                                if (translateLoading) {
                                    ProgressIndicatorLoading(
                                        progressIndicatorSize = 20.dp,
                                        progressIndicatorColor = Color.White,
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Text(
                                        text = "Loading...",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            Color.White,
                                            fontSize = 4.em,
                                        )
                                    )
                                } else {
                                    Icon(
                                        Icons.Rounded.Translate,
                                        contentDescription = "translate_icon",
                                        tint = Color.White,
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Text(
                                        text = "($languageCode)",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            Color.White,
                                            fontSize = 4.em,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }.let { animation ->
                    LaunchedEffect(animation) {
                        state = true
                    }
                }
            }
        }
    }

    @Composable
    fun ProgressIndicatorLoading(progressIndicatorSize: Dp, progressIndicatorColor: Color) {

        val infiniteTransition = rememberInfiniteTransition(label = "")

        val angle by infiniteTransition.animateFloat(
            label = "FloatAnimation",
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 600
                }
            ),
        )

        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier
                .size(progressIndicatorSize)
                .rotate(angle)
                .border(
                    4.dp,
                    brush = Brush.sweepGradient(
                        listOf(
                            Color.White, // add background color first
                            progressIndicatorColor.copy(alpha = 0.1f),
                            progressIndicatorColor
                        )
                    ),
                    shape = CircleShape
                ),
            strokeWidth = 1.dp,
            color = Color.White // Set background color
        )
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }
}

