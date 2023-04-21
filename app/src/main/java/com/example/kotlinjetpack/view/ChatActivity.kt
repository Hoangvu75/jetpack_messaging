package com.example.kotlinjetpack.view

import AddChatRequestBody
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpack.const.BASE_URL
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.model.Chat
import com.example.kotlinjetpack.model.User
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.AddChatViewModel
import com.example.kotlinjetpack.view_model.GetChatViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .background(primaryColor)
                        .padding(10.dp)
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.fillMaxWidth(1f),
                        style = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Start,
                            color = Color.White,
                        ),
                    )
                    Text(
                        text = phone,
                        modifier = Modifier.fillMaxWidth(1f),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Start,
                            color = Color.White.copy(alpha = 0.7f),
                            fontStyle = FontStyle(1),
                        ),
                    )
                }

                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = listState
                ) {
                    items(chatList.size) { index ->
                        ChatElement(
                            sender = chatList[index].sender!!,
                            content = chatList[index].content!!,
                        )
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        listState.scrollToItem(chatList.size)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
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
}

@Composable
fun ChatElement(
    sender: String,
    content: String
) {
    if (sender == PHONE) {
        Row {
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = primaryColor,
                modifier = Modifier.padding(top = 5.dp, start = 100.dp, end = 10.dp, bottom = 5.dp)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(all = 10.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(Color.White)
                )
            }
        }
    } else {
        Row {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = greyTextColor,
                modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 100.dp, bottom = 5.dp)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(all = 10.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(Color.White)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

