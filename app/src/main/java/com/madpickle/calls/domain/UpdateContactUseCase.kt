package com.madpickle.calls.domain

import android.content.Context
import android.graphics.Bitmap
import contacts.core.Contacts
import contacts.core.entities.NewPhone
import contacts.core.entities.PhoneEntity
import contacts.core.equalToIgnoreCase
import contacts.core.util.PhotoData
import contacts.core.util.addPhone
import contacts.core.util.removeAllPhones
import contacts.core.util.setName
import contacts.core.util.setPhoto

class UpdateContactUseCase(private val context: Context) {

    operator fun invoke(contactId: Long, name: String, bitmap: Bitmap?, phoneNumbers: List<String>) {
        val contactsApi = Contacts(context)
        val contact = contactsApi.query()
            .where {
                this.RawContact.Id equalToIgnoreCase contactId.toString()
            }
            .limit(1)
            .find().firstOrNull()
        if(contact == null) return

        contactsApi
            .update()
            .deleteBlanks(true)
            .contacts(contact.mutableCopy {
                setName {
                    displayName = name
                }
                if(bitmap != null) {
                    setPhoto(PhotoData.Companion.from(bitmap))
                }
                removeAllPhones()
                phoneNumbers.forEach {
                    addPhone(NewPhone(
                        type = PhoneEntity.Type.MOBILE,
                        number = it,
                    ))
                }
            })
            .commit()
    }

}