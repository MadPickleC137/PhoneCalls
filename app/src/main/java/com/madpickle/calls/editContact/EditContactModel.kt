package com.madpickle.calls.editContact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ContactDraft
import com.madpickle.calls.data.ImageType
import com.madpickle.calls.data.getResByType
import com.madpickle.calls.domain.LoadContactsUseCase
import com.madpickle.calls.domain.SaveContactUseCase
import com.madpickle.calls.domain.UpdateContactUseCase
import com.madpickle.calls.utils.isValidPhone
import com.madpickle.calls.utils.onlyDigits
import kotlinx.coroutines.launch


class EditContactModel(
    private val context: Context,
    private val draft: ContactDraft?,
    private val saveContactUseCase: SaveContactUseCase = SaveContactUseCase(context),
    private val updateContactUseCase: UpdateContactUseCase = UpdateContactUseCase(context)
) : ScreenModel {
    private val prefix = "7"
    val isSuccess = mutableStateOf(false)
    private val userImage = mutableStateOf<Bitmap?>(null)
    private val numberStates: MutableList<MutableState<String>> = mutableListOf()
    private val phonePrimary = mutableStateOf(prefix)
    private val username = mutableStateOf("")
    private val phoneSecondary = mutableStateOf(prefix)
    val isErrorName: MutableState<Boolean> = mutableStateOf(false)
    val isErrorPhone: MutableState<Boolean> = mutableStateOf(false)
    private val imagesBitmap = mutableListOf<Bitmap>()

    init {
        initNumbers(draft?.numbers.orEmpty())
        setUsername(draft?.name.orEmpty())
        initBitmaps()
    }

    private fun initBitmaps() {
        try {
            draft?.imageUri?.let {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(context.contentResolver, draft.imageUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                userImage.value = bitmap
            }
            ImageType.entries.forEach {
                val bitmap: Bitmap? = BitmapFactory.decodeResource(context.resources, it.getResByType())

                if (bitmap != null) {
                    imagesBitmap.add(bitmap)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getImages() = imagesBitmap

    fun getUserImage(): Bitmap? = userImage.value

    fun isSelectedBitmap(bitmap: Bitmap): Boolean {
        return userImage.value == bitmap
    }

    fun setUserImage(bitmap: Bitmap){
        if(userImage.value != bitmap) {
            userImage.value = bitmap
        }
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
        if (isErrorName.value) return@launch
        if (isErrorPhone.value) return@launch
        val listNumbers = numberStates.map { it.value }.ifEmpty {
            listOf(
                if (getPrimaryPhone() != prefix) getPrimaryPhone() else "",
                if (getSecondaryPhone() != prefix) getSecondaryPhone() else ""
            ).filter { it.isNotEmpty() }
        }
        if (draft == null) {
            isSuccess.value = saveContactUseCase(
                username.value,
                userImage.value,
                listNumbers
            )
        } else {
            updateContactUseCase(
                draft.id,
                username.value,
                userImage.value,
                listNumbers
            )
        }
        LoadContactsUseCase.load(context)
    }

}