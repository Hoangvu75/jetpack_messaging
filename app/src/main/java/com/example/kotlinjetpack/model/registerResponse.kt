package com.example.kotlinjetpack.model

data class RegisterResponse (
    val success: Boolean,
    val message: String,
    val registeredAccount: RegisteredAccount
)

data class RegisteredAccount (
    val phone: String,
    val password: String,
    val _id: String
)