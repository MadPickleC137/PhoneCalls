package com.madpickle.calls.data

data class ItemContact(
    val id: Long,
    val name: String,
    val number: String,
    val imageUri: String?,
    val position: Int,
)