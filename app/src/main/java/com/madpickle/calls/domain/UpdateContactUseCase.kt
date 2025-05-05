package com.madpickle.calls.domain

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract

class UpdateContactUseCase(private val contentResolver: ContentResolver) {

    operator fun invoke(name: String, phoneNumbers: List<String>) {
        val uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(name))
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                if(index < 0) return@use
                // Получаем ID контакта
                val contactId: String = it.getString(index) ?: return@use
                // Удаляем старый номер (если нужно) и добавляем новый
                val phoneUri = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, contactId.toLong().toString())
                contentResolver.delete(phoneUri, "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?", arrayOf(contactId))

                // Добавление номеров телефонов
                for (phoneNumber in phoneNumbers) {
                    val phoneValues = ContentValues().apply {
                        put(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContentUris.parseId(uri))
                        put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                        put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    }
                    contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)
                }
            }
        }
    }

}