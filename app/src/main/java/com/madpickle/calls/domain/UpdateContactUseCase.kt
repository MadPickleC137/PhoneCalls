package com.madpickle.calls.domain

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract

class UpdateContactUseCase(private val contentResolver: ContentResolver) {

    operator fun invoke(contactId: String, name: String, phoneNumbers: List<String>) {
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        // Создаем объект ContentValues для обновления
        val nameValues = ContentValues().apply {
            put(ContactsContract.Contacts.DISPLAY_NAME, name)
        }
        contentResolver.update(uri, nameValues, null, null)
        // Удаляем старые номера телефонов
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneSelection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        contentResolver.delete(phoneUri, phoneSelection, arrayOf(contactId))
        // Добавляем новые номера телефонов
        for (phoneNumber in phoneNumbers) {
            val phoneValues = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.Phone.CONTACT_ID, contactId)
                put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) // или другой тип
            }
            contentResolver.insert(phoneUri, phoneValues)
        }
    }

}