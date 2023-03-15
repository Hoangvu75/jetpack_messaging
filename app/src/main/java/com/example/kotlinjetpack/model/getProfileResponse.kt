package com.example.kotlinjetpack.model

data class GetProfileResponse (
    val success: Boolean,
    val message: String,
    val profile: Profile
)

data class Profile (
    val id: String,
    val phone: String,
    val name: String,
    val birthday: String,
    val avatar: String,
    val v: Long
)
