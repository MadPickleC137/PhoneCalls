package com.madpickle.calls.contacts

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.domain.ContactsContentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsModel(
    private val contentResolver: ContentResolver,
) : ScreenModel {
    private val _contacts = MutableStateFlow<List<ItemContactState>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun loadContacts() = screenModelScope.launch {
        val contacts = ContactsContentProvider.getContacts(contentResolver)
        _contacts.value = contacts.sortedBy { it.name }.distinctBy { it.name }.map {
            ItemContactState(
                it.name, it.imageUri, null, it.position
            )
        }
    }
}

data class ItemContactState(
    val name: String,
    val imageUri: String?,
    val number: String? = null,
    val position: Int,
)