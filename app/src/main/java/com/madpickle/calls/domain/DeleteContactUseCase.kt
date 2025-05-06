package com.madpickle.calls.domain

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract


class DeleteContactUseCase(private val contentResolver: ContentResolver) {

    operator fun invoke(ids: List<String>) {
        ids.toSet().forEach { id ->
            try {
                val uri: Uri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, id)
                val rows = contentResolver.delete(uri, null, null)
                if(rows == 0) {
                    val uriAlt = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
                    contentResolver.delete(uriAlt, null, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}