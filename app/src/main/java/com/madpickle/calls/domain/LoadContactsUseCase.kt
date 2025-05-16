package com.madpickle.calls.domain

import android.content.Context
import android.telephony.PhoneNumberUtils
import com.madpickle.calls.data.ItemContact
import contacts.core.Contacts
import contacts.core.ContactsFields
import contacts.core.asc
import contacts.core.util.phoneList


object LoadContactsUseCase {
    private val contacts = mutableListOf<ItemContact>()

    fun getContacts(context: Context): MutableList<ItemContact> {
        if (contacts.isEmpty()) {
            load(context)
        }
        return contacts
    }

    fun load(context: Context) {
        contacts.clear()
        val result = Contacts(context)
            .query()
            .orderBy(ContactsFields.DisplayNamePrimary.asc(true))
            .find()
        result.map { contact ->
            contacts.add(
                ItemContact(
                    id = contact.id,
                    name = contact.displayNamePrimary ?: contact.displayNameAlt ?: "",
                    numbers = contact.phoneList()
                        .mapNotNull { PhoneNumberUtils.formatNumber(it.number, "RU") },
                    imageUri = contact.photoUri,
                )
            )
        }
    }
}