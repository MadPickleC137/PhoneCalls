package com.madpickle.calls.domain

import android.content.Context
import android.graphics.Bitmap
import contacts.core.Contacts
import contacts.core.entities.NewName
import contacts.core.entities.NewOrganization
import contacts.core.entities.NewPhone
import contacts.core.entities.NewRawContact
import contacts.core.entities.PhoneEntity
import contacts.core.util.PhotoData
import contacts.core.util.setPhoto

class SaveContactUseCase(private val context: Context) {

    operator fun invoke(name: String, bitmap: Bitmap, phoneNumbers: List<String>): Boolean {
        val insertResult = Contacts(context)
            .insert()
            .rawContacts(
                NewRawContact(
                    name = NewName(givenName = name,),
                    organization = NewOrganization(
                        company = "",
                        title = ""
                    ),
                    phones = phoneNumbers.map {
                        NewPhone(
                            type = PhoneEntity.Type.MOBILE,
                            number = it,
                        )
                    }.toMutableList()
                ).apply {
                    setPhoto(PhotoData.Companion.from(bitmap))
                }
            )
            .commit()
        return insertResult.isSuccessful
    }
}