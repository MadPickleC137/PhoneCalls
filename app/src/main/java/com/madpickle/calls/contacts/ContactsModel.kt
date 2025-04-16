package com.madpickle.calls.contacts

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.data.ItemContact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsModel(
    private val contentResolver: ContentResolver,
): ScreenModel {
    private val _contacts = MutableStateFlow<List<ItemContact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun loadContacts() = screenModelScope.launch {
        _contacts.value = ContactsContentProvider.getContacts(contentResolver).sortedBy { it.name }
    }
}