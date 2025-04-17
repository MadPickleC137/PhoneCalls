package com.madpickle.calls.data

data class ContactDetail(
    val callLogs: List<ItemCallLog> = emptyList(),
    val contact: ItemContact? = null,
)