package com.madpickle.calls.detail

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.data.ContactDetail
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.domain.DeleteContactUseCase
import com.madpickle.calls.domain.ImageSavedContact

class DetailModel(
    private val context: Context,
    private val deleteContactUseCase: DeleteContactUseCase = DeleteContactUseCase(context.contentResolver)
) : ScreenModel {
    fun getDetail(name: String): ContactDetail {
        val contacts = ContactsContentProvider.getContacts(context.contentResolver).filter {
            it.name == name
        }.distinctBy { it.number }
        val calls = CallsLogContentProvider.getCalls(context.contentResolver).filter {
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
            image = ImageSavedContact(context).getImageByName(name),
            sectionsLogs = calls
        )
    }

    fun deleteContact(ids: List<String>) {
        deleteContactUseCase(ids)
        ContactsContentProvider.loadContacts(context.contentResolver)
    }
}