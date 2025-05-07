package com.madpickle.calls.dial

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ItemContact
import com.madpickle.calls.domain.LoadContactsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DialModel(
    context: Context,
) : ScreenModel {
    private val cachedContacts = LoadContactsUseCase.getContacts(context)
    private val _contacts =
        MutableStateFlow<List<ItemContact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun findInContacts(phone: String) {
        screenModelScope.launch {
            _contacts.value = cachedContacts
                .map { contact ->
                    contact.copy(
                        numbers = listOf(contact.numbers.firstOrNull { it.contains(phone) }.orEmpty())
                    )
                }.filter { it.numbers.isNotEmpty() }
        }
    }

}