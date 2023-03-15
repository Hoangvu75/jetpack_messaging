package com.example.kotlinjetpack.model

data class GetChatResponse (
    val success: Boolean?,
    val chat: ChatItem?,
    val message: String?
)

