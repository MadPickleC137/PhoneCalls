package com.madpickle.calls.data

data class ContactDetail(
    val sectionsLogs: Map<String, List<ItemCallLog>> = emptyMap(),
    val numbers: List<String> = emptyList(),
    val ids: List<Long> = emptyList(),
    val name: String,
    val imageUri: String?,
)