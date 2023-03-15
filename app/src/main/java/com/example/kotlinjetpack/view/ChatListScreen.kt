package com.example.kotlinjetpack.view

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.model.ChatItem
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.GetChatListViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ChatListScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current

    val chatItemList = remember { mutableStateListOf<ChatItem>() }

    if (chatItemList.isEmpty()) {
        val getChatListViewModel = GetChatListViewModel()
        getChatListViewModel.getChatList()
        getChatListViewModel.resultLiveData.observe(lifecycleOwner) { result ->
            when (result) {
                0 -> {
                    println("Loading")
                }
                1 -> {
                    println("Success, chat: ${getChatListViewModel.getChatListData!!.chat_list}")
                    chatItemList.clear()
                    chatItemList.addAll(getChatListViewModel.getChatListData!!.chat_list!!)
                }
                2 -> {
                    println("Error: ${getChatListViewModel.errorMessage}")
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
                Text(
                    text = "Chat",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.displayMedium.copy(
                        textAlign = TextAlign.Start,
                        color = defaultTextColor,
                    ),
                )

                LazyColumn(
                    modifier = Modifier.weight(1f, false)
                ) {
                    items(chatItemList.size) { index ->
                        val name0 = chatItemList[index].users!![0].name!!
                        val name1 = chatItemList[index].users!![1].name!!
                        val nameList: List<String> = listOf(name0, name1)

                        val phone0 = chatItemList[index].users!![0].phone!!
                        val phone1 = chatItemList[index].users!![1].phone!!
                        val phoneList: List<String> = listOf(phone0, phone1)

                        var lastMessageContent = ""
                        var lastMessageTime: Long = 0
                        var timeString = ""
                        if (chatItemList[index].chat!!.isNotEmpty()) {
                            lastMessageContent = chatItemList[index].chat!!.last().content!!
                            lastMessageTime = chatItemList[index].chat!!.last().time!!
                            val formatter = SimpleDateFormat("dd/MM     hh:mm")
                            timeString = formatter.format(lastMessageTime)
                        }

                        ChatItem(
                            nameList = nameList,
                            phoneList = phoneList,
                            lastMessageContent = lastMessageContent,
                            lastMessageTime = timeString,
                            chatId = chatItemList[index]._id!!
                        )
                    }
                }
            }
        }
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
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, primaryColor, RoundedCornerShape(8.dp))
            .background(color = primaryColor.copy(alpha = 0.1f))
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("chatId", chatId)
                intent.putExtra("phone", friendPhone)
                context.startActivity(intent)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row {
                    Text(
                        text = friendName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Start,
                            color = defaultTextColor,
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


