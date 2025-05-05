package com.madpickle.calls.domain

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.madpickle.calls.data.ImageType

class ImageSavedContact(private val context: Context) {
    companion object {
        private const val PREF_NAME = "ImageContactPreferences"
    }

    fun getImageByName(name: String): ImageType {
        val sp = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val codeImage = sp.getString(name, "")
        return try {
            ImageType.valueOf(codeImage!!)
        } catch (e: Exception) {
            ImageType.Default
        }
    }

    fun setImageByName(name: String, imageType: ImageType) {
        val sp = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        sp.edit {
            putString(name, imageType.name)
        }
    }
}