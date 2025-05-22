package com.madpickle.calls.domain

import android.content.Context
import android.graphics.Bitmap
import contacts.core.Contacts
import contacts.core.entities.NewPhone
import contacts.core.entities.PhoneEntity
import contacts.core.equalToIgnoreCase
import contacts.core.log.AndroidLogger
import contacts.core.util.PhotoData
import contacts.core.util.addPhone
import contacts.core.util.removeAllPhones
import contacts.core.util.setName
import contacts.core.util.setPhoto
import contacts.permissions.rawContactsQueryWithPermission
import contacts.permissions.updateWithPermission

class UpdateContactUseCase(private val context: Context) {

    suspend operator fun invoke(contactId: Long, name: String, bitmap: Bitmap?, phoneNumbers: List<String>): Boolean {
        val contactsApi = Contacts(context, logger = AndroidLogger())
        val contact = contactsApi.rawContactsQueryWithPermission()
            .where {
                this.RawContact.Id equalToIgnoreCase contactId.toString()
            }
            .limit(1)
            .find().firstOrNull()
        if(contact == null) return false

        return contactsApi
            .updateWithPermission()
            .rawContacts(contact.mutableCopy {
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
            .commit().isSuccessful
    }

}