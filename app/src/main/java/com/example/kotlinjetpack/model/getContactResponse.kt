package com.example.kotlinjetpack.model

data class GetContactResponse (
    val success: Boolean?,
    val contact: Contact?,
    val message: String?
)

data class Contact (
    val _id: String?,
    val phone: String?,
    val contactList: List<ContactItem>?
)

data class ContactItem (
    val phone: String?,
    val name: String?,
    val _id: String?
)