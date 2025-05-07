package com.madpickle.calls.data

data class ItemContact(
    val id: Long,
    val name: String,
    val numbers: List<String>,
    val imageUri: String?,
)