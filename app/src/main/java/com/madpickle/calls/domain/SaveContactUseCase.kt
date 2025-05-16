package com.madpickle.calls.domain

import android.content.Context
import android.graphics.Bitmap
import contacts.core.Contacts
import contacts.core.entities.NewName
import contacts.core.entities.NewNickname
import contacts.core.entities.NewPhone
import contacts.core.entities.PhoneEntity
import contacts.core.log.AndroidLogger
import contacts.core.util.PhotoData
import contacts.core.util.addPhone
import contacts.core.util.setName
import contacts.core.util.setNickname
import contacts.core.util.setPhoto
import contacts.permissions.insertWithPermission

class SaveContactUseCase(private val context: Context) {

    suspend operator fun invoke(name: String, bitmap: Bitmap?, phoneNumbers: List<String>): Boolean {
        val insertResult = Contacts(context, logger = AndroidLogger())
            .insertWithPermission()
            .allowBlanks(true)
            .validateAccounts(false)
            .validateGroupMemberships(false)
            .rawContact {
                setNickname(NewNickname(name))
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