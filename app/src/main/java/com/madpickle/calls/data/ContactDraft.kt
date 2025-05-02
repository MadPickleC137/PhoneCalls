package com.madpickle.calls.data

data class ContactDraft(
    val name: String,
    val image: ImageType,
    val ids: List<String>,
    val numbers: List<String>,
)
