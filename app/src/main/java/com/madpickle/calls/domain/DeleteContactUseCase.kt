package com.madpickle.calls.domain

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log


class DeleteContactUseCase(private val contentResolver: ContentResolver) {

    private fun contactExists(contactId: String): Boolean {
        val cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            "${ContactsContract.RawContacts.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )
        val result = cursor?.moveToFirst() == true
        cursor?.close()
        return result
    }

    operator fun invoke(ids: List<String>) {
        ids.forEach { id ->
            if(contactExists(id)) {
                try {
                    val uri: Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
                    contentResolver.delete(uri, null, null)
                } catch (e: Exception) {
                    Log.d("DeleteContactUseCase", e.message.toString())
                }
            }
        }
    }
}