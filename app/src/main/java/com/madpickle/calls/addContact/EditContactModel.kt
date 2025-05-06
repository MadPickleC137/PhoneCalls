package com.madpickle.calls.addContact

import android.content.ContentResolver
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ContactDraft
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.domain.DeleteContactUseCase
import com.madpickle.calls.domain.SaveContactUseCase
import com.madpickle.calls.utils.isValidPhone
import com.madpickle.calls.utils.onlyDigits
import kotlinx.coroutines.launch

class EditContactModel(
    private val cr: ContentResolver,
    private val draft: ContactDraft?,
    private val saveContactUseCase: SaveContactUseCase = SaveContactUseCase(cr),
    private val deleteContactUseCase: DeleteContactUseCase = DeleteContactUseCase(cr)
): ScreenModel {
    private val prefix = "7"
    val isSuccess = mutableStateOf(false)
    private val numberStates: MutableList<MutableState<String>> = mutableListOf()
    private val phonePrimary = mutableStateOf(prefix)
    private val username = mutableStateOf("")
    private val phoneSecondary =  mutableStateOf(prefix)
    val isErrorName: MutableState<Boolean> = mutableStateOf(false)
    val isErrorPhone: MutableState<Boolean> = mutableStateOf(false)

    init {
        initNumbers(draft?.numbers.orEmpty())
        setUsername(draft?.name.orEmpty())
    }

    fun setPrimaryPhone(newValue: String) {
        isErrorPhone.value = !newValue.isValidPhone()
        phonePrimary.value = newValue
    }

    fun setSecondaryPhone(newValue: String) {
        phoneSecondary.value = newValue
    }
    fun setUsername(newValue: String) {
        isErrorName.value = newValue.length < 3
        username.value = newValue
    }

    fun getUsername() = username.value
    fun getPrimaryPhone() = phonePrimary.value
    fun getSecondaryPhone() = phoneSecondary.value

    private fun initNumbers(numbers: List<String>) {
        numberStates.clear()
        numbers.forEach {
            numberStates.add(mutableStateOf(it.onlyDigits()))
        }
    }

    fun getSavedNumbers() = numberStates

    fun onSave() = screenModelScope.launch {
        if(isErrorName.value) return@launch
        if(isErrorPhone.value) return@launch
        //удалить, если существует, перед тем как создать новый контакт с измененными данными
        if(draft?.ids != null) {
            deleteContactUseCase(draft.ids)
        }
        val listNumbers = numberStates.map { it.value }.ifEmpty {
            listOf(
                if(getPrimaryPhone() != prefix) getPrimaryPhone() else "",
                if(getSecondaryPhone()  != prefix) getSecondaryPhone() else ""
            ).filter { it.isNotEmpty() }
        }
        saveContactUseCase(username.value, listNumbers)
        ContactsContentProvider.loadContacts(cr)
        isSuccess.value = true
    }

}