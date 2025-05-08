package com.madpickle.calls.domain

import android.content.Context
import android.util.Log
import contacts.core.Contacts
import contacts.core.equalToIgnoreCase


class DeleteContactUseCase(private val context: Context) {

    operator fun invoke(id: String) {
        val contactApi = Contacts(context)
        val contacts = Contacts(context).rawContactsQuery()
            .where {
                RawContact.Id equalToIgnoreCase id
            }
            .find()
        val result = contactApi
            .delete()
            .rawContacts(contacts)
            .commit()
        Log.d("DeleteContactUseCase", result.isSuccessful.toString())
    }
}