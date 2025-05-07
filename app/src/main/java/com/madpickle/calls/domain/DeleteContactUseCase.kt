package com.madpickle.calls.domain

import android.content.Context
import contacts.core.Contacts
import contacts.core.equalToIgnoreCase


class DeleteContactUseCase(private val context: Context) {

    operator fun invoke(id: Long) {
        Contacts(context)
            .delete()
            .rawContactsWhere { ContactId equalToIgnoreCase id.toString() }
            .commit()
    }
}