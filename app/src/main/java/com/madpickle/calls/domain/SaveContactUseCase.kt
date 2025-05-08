package com.madpickle.calls.domain

import android.accounts.Account
import android.content.Context
import android.graphics.Bitmap
import contacts.core.Contacts
import contacts.core.entities.NewName
import contacts.core.entities.NewNickname
import contacts.core.entities.NewPhone
import contacts.core.entities.NewRawContact
import contacts.core.entities.PhoneEntity
import contacts.core.log.AndroidLogger
import contacts.core.util.PhotoData
import contacts.core.util.addPhone
import contacts.core.util.setAccount
import contacts.core.util.setName
import contacts.core.util.setPhoto

class SaveContactUseCase(private val context: Context) {

    operator fun invoke(name: String, bitmap: Bitmap?, phoneNumbers: List<String>): Boolean {
        val insertResult = Contacts(context, logger = AndroidLogger())
            .insert()
            .allowBlanks(true)
            .validateAccounts(false)
            .validateGroupMemberships(false)
            .rawContact {
                setAccount(Account("vestrel00@ecff.asd", "com.pixar"))
                setName(NewName().apply {
                    displayName = name
                    nickname = NewNickname(name)
                    givenName = name
                })
                phoneNumbers.map {
                    addPhone(NewPhone().apply {
                        type = PhoneEntity.Type.MOBILE
                        number = it
                        label = it
                    })
                }
                if (bitmap != null) {
                    setPhoto(PhotoData.Companion.from(bitmap))
                }
            }
            .commit()
        return insertResult.isSuccessful
    }
}