package com.madpickle.calls.domain

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.util.Log


class DeleteContactUseCase(private val contentResolver: ContentResolver) {
    operator fun invoke(name: String) {
        val contactUri: Uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(name))
        val cur = contentResolver.query(contactUri, null, null, null, null)
            ?: return
        try {
            while (cur.moveToNext()) {
                if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME).coerceAtLeast(0))
                        .equals(name, ignoreCase = true)
                ) {
                    val lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY).coerceAtLeast(0))
                    val uri: Uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        lookupKey
                    )
                    contentResolver.delete(uri, null, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cur.close()
        }
    }
}