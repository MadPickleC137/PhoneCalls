package com.madpickle.calls.domain

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.ContactsContract

class SaveContactUseCase(private val contentResolver: ContentResolver) {

    operator fun invoke(name: String, phoneNumbers: List<String>) {
        // Создаем новый контакт
        val contactValues = ContentValues().apply {
            putNull(ContactsContract.RawContacts.ACCOUNT_TYPE)
            putNull(ContactsContract.RawContacts.ACCOUNT_NAME)
        }

        val contactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contactValues)
        val contactId = contactUri?.lastPathSegment

        if (contactId != null) {
            // Устанавливаем имя контакта
            val nameValues = ContentValues().apply {
                put(ContactsContract.Data.RAW_CONTACT_ID, contactId.toLong()) // Устанавливаем raw_contact_id
                put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) // Указываем MIME тип
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            }

            contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameValues)

            // Добавляем номера телефонов
            for (phoneNumber in phoneNumbers) {
                val phoneValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, contactId.toLong()) // Устанавливаем raw_contact_id
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) // Указываем MIME тип
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                    put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) // или другой тип
                }
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)
            }
        }
    }
}