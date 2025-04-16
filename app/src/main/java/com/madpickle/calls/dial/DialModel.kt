package com.madpickle.calls.dial

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ItemContact
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.utils.formatPhoneNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DialModel(
    contentResolver: ContentResolver,
) : ScreenModel {
    private val cachedContacts = ContactsContentProvider.getContacts(contentResolver)
    private val _contacts =
        MutableStateFlow<List<ItemContact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun findInContacts(phone: String) {
        screenModelScope.launch {
            _contacts.value = cachedContacts.filter { it.number.formatPhoneNumber().contains(phone) }
        }
    }

}