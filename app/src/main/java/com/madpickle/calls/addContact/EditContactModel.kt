package com.madpickle.calls.addContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.madpickle.calls.utils.isValidPhone
import com.madpickle.calls.utils.onlyDigits

class EditContactModel: ScreenModel {

    private val numberStates: MutableList<MutableState<String>> = mutableListOf()
    private val phonePrimary = mutableStateOf("7")
    private val username = mutableStateOf("")
    private val phoneSecondary =  mutableStateOf("7")
    val isErrorName: MutableState<Boolean> = mutableStateOf(false)
    val isErrorPhone: MutableState<Boolean> = mutableStateOf(false)

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

    fun initNumbers(numbers: List<String>) {
        numberStates.clear()
        numbers.forEach {
            numberStates.add(mutableStateOf(it.onlyDigits()))
        }
    }

    fun getSavedNumbers() = numberStates

    fun onSave() {

    }

}