package com.example.kotlinjetpack.model

data class DeleteContactResponse (
    val success: Boolean?,
    val contact: Contact?,
    val message: String?
)