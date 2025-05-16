package com.madpickle.calls.domain

import android.content.Context
import contacts.core.Contacts
import contacts.permissions.deleteWithPermission


class DeleteContactUseCase(private val context: Context) {

    suspend operator fun invoke(id: Long): Boolean {
        val result = Contacts(context)
            .deleteWithPermission()
            .contactsWithId(id)
            .commit()
        return result.isSuccessful
    }
}