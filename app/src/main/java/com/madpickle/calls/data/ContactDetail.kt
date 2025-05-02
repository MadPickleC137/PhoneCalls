package com.madpickle.calls.data

data class ContactDetail(
    val sectionsLogs: Map<String, List<ItemCallLog>> = emptyMap(),
    val numbers: List<String> = emptyList(),
    val ids: List<String> = emptyList(),
    val image: ImageType,
    val name: String,
    val imageUri: String?,
)