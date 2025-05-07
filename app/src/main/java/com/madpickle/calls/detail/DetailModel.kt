package com.madpickle.calls.detail

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.data.ContactDetail
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.DeleteContactUseCase
import com.madpickle.calls.domain.GetContactUseCase
import com.madpickle.calls.domain.LoadContactsUseCase
import contacts.core.Contacts
import contacts.core.entities.EmailEntity
import contacts.core.entities.NewEmail
import contacts.core.entities.NewName
import contacts.core.entities.NewOrganization
import contacts.core.entities.NewRawContact
import contacts.core.util.PhotoData
import contacts.core.util.phoneList
import contacts.core.util.setPhoto

class DetailModel(
    private val context: Context,
    private val id: Long,
    private val getContactUseCase: GetContactUseCase = GetContactUseCase(context),
    private val deleteContactUseCase: DeleteContactUseCase = DeleteContactUseCase(context)
) : ScreenModel {

    val detail: MutableState<ContactDetail> = mutableStateOf(
        ContactDetail(id = id)
    )

    init {
        setDetail()
    }

    private fun setDetail() {
        val contact = getContactUseCase(id) ?: return

        val calls = CallsLogContentProvider.getCalls(context.contentResolver).filter {
            it.id == id.toString()
        }
            .sortedBy { it.date }
            .asReversed()
            .groupBy { it.getDateFormatted() }
        detail.value = ContactDetail(
            name = contact.displayNamePrimary ?: contact.displayNameAlt ?: "",
            imageUri = contact.photoThumbnailUri?.toString(),
            numbers = contact.phoneList().mapNotNull { it.number },
            id = contact.id,
            sectionsLogs = calls
        )
    }

    fun deleteContact(id: Long) {
        deleteContactUseCase(id)
        LoadContactsUseCase.load(context)
    }

    fun checkSaveImage(bitmap: Bitmap) {
        val insertResult = Contacts(context)
            .insert()
            .rawContacts(
                NewRawContact(
                    name = NewName(
                        givenName = "John",
                        familyName = "Doe"
                    ),
                    organization = NewOrganization(
                        company = "Amazon",
                        title = "Superstar"
                    ),
                    emails = mutableListOf(
                        NewEmail(
                            address = "john.doe@amazon.com",
                            type = EmailEntity.Type.WORK
                        )
                    )
                ).apply {
                    setPhoto(PhotoData.Companion.from(bitmap))
                }
            )
            .commit()
    }
}