package com.example.kotlinjetpack.model

data class AddContactResponse (
    val success: Boolean?,
    val contact: Contact?,
    val message: String?
)