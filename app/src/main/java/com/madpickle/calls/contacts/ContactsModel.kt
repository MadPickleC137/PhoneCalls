package com.madpickle.calls.contacts

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ItemContact
import com.madpickle.calls.domain.LoadContactsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsModel(
    private val context: Context,
) : ScreenModel {
    private val _contacts = MutableStateFlow<List<ItemContact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun loadContacts() = screenModelScope.launch {
        _contacts.value = LoadContactsUseCase.getContacts(context)
    }
}