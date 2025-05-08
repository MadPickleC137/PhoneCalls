package com.madpickle.calls.detail

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.data.ContactDetail
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.DeleteContactUseCase
import com.madpickle.calls.domain.GetContactUseCase
import com.madpickle.calls.domain.LoadContactsUseCase
import contacts.core.util.phoneList

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
            it.id == contact.id.toString()
        }
            .sortedBy { it.date }
            .asReversed()
            .groupBy { it.getDateFormatted() }
        detail.value = ContactDetail(
            name = contact.displayNamePrimary ?: contact.displayNameAlt ?: "",
            imageUri = contact.photoThumbnailUri,
            numbers = contact.phoneList().mapNotNull { it.number },
            id = id,
            sectionsLogs = calls
        )
    }

    fun deleteContact() {
        deleteContactUseCase(id.toString())
        LoadContactsUseCase.load(context)
    }
}