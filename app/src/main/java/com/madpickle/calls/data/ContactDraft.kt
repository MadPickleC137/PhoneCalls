package com.madpickle.calls.data

import android.net.Uri

data class ContactDraft(
    val name: String,
    val imageUri: Uri?,
    val id: Long,
    val numbers: List<String>,
)
