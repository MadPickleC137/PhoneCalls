package com.madpickle.calls.data

import android.graphics.Bitmap
import android.net.Uri

data class ContactDetail(
    val sectionsLogs: Map<String, List<ItemCallLog>> = emptyMap(),
    val numbers: List<String> = emptyList(),
    val id: Long,
    val name: String = "",
    val imageProfile: Bitmap? = null,
    val imageUri: Uri? = null,
)