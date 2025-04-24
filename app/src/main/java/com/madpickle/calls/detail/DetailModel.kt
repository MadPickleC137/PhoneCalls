package com.madpickle.calls.detail

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.data.ContactDetail
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.domain.DeleteContactUseCase

class DetailModel(
    private val contentResolver: ContentResolver,
    private val deleteContactUseCase: DeleteContactUseCase = DeleteContactUseCase(contentResolver)
): ScreenModel {
    fun getDetail(name: String): ContactDetail {
        val contacts = ContactsContentProvider.getContacts(contentResolver).filter {
            it.name == name
        }.distinctBy { it.number }
        val calls = CallsLogContentProvider.getCalls(contentResolver).filter {
            it.name == name
        }
            .sortedBy { it.date }
            .asReversed()
            .groupBy { it.getDateFormatted() }
        return ContactDetail(
            name = contacts.firstOrNull()?.name.orEmpty(),
            imageUri = contacts.firstOrNull { it.imageUri != null }?.imageUri,
            numbers = contacts.map { it.number },
            ids = contacts.map { it.id },
            sectionsLogs = calls
        )
    }

    fun deleteContact(ids: List<String>) {
        deleteContactUseCase(ids)
        ContactsContentProvider.loadContacts(contentResolver)
    }
}