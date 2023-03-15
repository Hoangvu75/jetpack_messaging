package com.example.kotlinjetpack.model

data class GetChatListResponse(
    val success: Boolean?,
    val chat_list: List<ChatItem>?,
    val message: String?
)

data class ChatItem(
    val _id: String?,
    val users: List<User>?,
    val chat: List<Chat>?,
)

data class Chat(
    val sender: String?,
    val content: String?,
    val time: Long?,
    val _id: String?
)

data class User(
    val phone: String?,
    val name: String?,
    val _id: String?
)
