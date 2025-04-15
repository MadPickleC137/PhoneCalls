package com.madpickle.calls.domain

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.madpickle.calls.data.ItemContact


class ContactsContentProvider(private val cr: ContentResolver) {
    private val order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    private val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    fun getAll(): List<ItemContact> {
        val crContacts: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            order
        )
        if(crContacts == null) return emptyList()
        val list = mutableListOf<ItemContact>()
        while (crContacts.moveToNext()) {
            val name =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME).coerceAtLeast(0))
            val phone =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER).coerceAtLeast(0))
            val photoUri =
                crContacts.getString(crContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI).coerceAtLeast(0))
            list.add(ItemContact(
                name = name,
                number = phone,
                imageUri = photoUri,
                position = crContacts.position
            ))
        }
        crContacts.close()
        return list
    }
}