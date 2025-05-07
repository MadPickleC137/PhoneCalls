package com.madpickle.calls.data

data class ContactDraft(
    val name: String,
    val imageUri: String?,
    val id: Long,
    val numbers: List<String>,
)
