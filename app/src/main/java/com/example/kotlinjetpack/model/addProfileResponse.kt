package com.example.kotlinjetpack.model

data class AddProfileResponse (
    val success: Boolean?,
    val message: String?,
    val addedProfile: AddedProfile?
)

data class AddedProfile (
    val _id: String?,
    val phone: String?,
    val name: String?,
    val birthday: String?,
    val avatar: String?,
)
