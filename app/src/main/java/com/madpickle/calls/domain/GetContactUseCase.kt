package com.madpickle.calls.domain

import android.content.Context
import contacts.core.Contacts
import contacts.core.entities.Contact
import contacts.core.equalToIgnoreCase

class GetContactUseCase(private val context: Context) {

    operator fun invoke(id: Long): Contact? {
        val result = Contacts(context).query()
            .where {
                this.RawContact.Id equalToIgnoreCase id.toString()
            }
            .include {
                setOf(
                    Contact.Id,
                    Contact.DisplayNamePrimary,
                    Phone.Number
                )
            }
            .limit(1)
            .find()
        return result.firstOrNull()
    }
}