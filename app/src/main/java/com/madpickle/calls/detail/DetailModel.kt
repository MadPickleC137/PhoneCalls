package com.madpickle.calls.detail

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.data.ContactDetail
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.ContactsContentProvider

class DetailModel(
    private val contentResolver: ContentResolver
): ScreenModel {
    fun getDetail(number: String): ContactDetail? {
        val contact = ContactsContentProvider.getContacts(contentResolver).firstOrNull {
            it.number == number
        } ?: return null
        val calls = CallsLogContentProvider.getCalls(contentResolver).filter {
            it.number == number
        }
        return ContactDetail(
            contact = contact,
            callLogs = calls
        )
    }

}