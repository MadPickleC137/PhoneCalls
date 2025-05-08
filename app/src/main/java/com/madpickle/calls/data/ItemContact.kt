package com.madpickle.calls.data

import android.net.Uri

data class ItemContact(
    val id: Long,
    val name: String,
    val numbers: List<String>,
    val imageUri: Uri?,
)