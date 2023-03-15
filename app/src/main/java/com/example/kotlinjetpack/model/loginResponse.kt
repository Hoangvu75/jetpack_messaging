package com.example.kotlinjetpack.model

data class LoginResponse (
    val success: Boolean?,
    val message: String?,
    val token: String?,
    val loginAccount: LoginAccount?,
)

data class LoginAccount (
    val _id: String?,
    val phone: String?,
    val password: String?,
)