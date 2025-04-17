package com.madpickle.calls.domain

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.madpickle.calls.data.ItemContact


object ContactsContentProvider {
    private val contacts = mutableListOf<ItemContact>()
    private const val ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    private val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
    )

    fun getContacts(cr: ContentResolver): MutableList<ItemContact> {
        if(contacts.isEmpty()) {
            loadContacts(cr)
        }
        return contacts
    }

    fun loadContacts(cr: ContentResolver) {
        contacts.clear()
        val crContacts: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ORDER
        )
        if(crContacts == null) return
        while (crContacts.moveToNext()) {
            val name =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME).coerceAtLeast(0))
            val phone =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER).coerceAtLeast(0))
            val photoUri =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI).coerceAtLeast(0))
            contacts.add(ItemContact(
                name = name,
                number = phone,
                imageUri = photoUri,
                position = crContacts.position
            ))
        }
        crContacts.close()
    }
}