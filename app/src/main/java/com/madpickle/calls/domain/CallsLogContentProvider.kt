package com.madpickle.calls.domain

import android.content.ContentResolver
import android.provider.CallLog
import com.madpickle.calls.data.ItemCallLog
import com.madpickle.calls.data.getCallType

class CallsLogContentProvider(private val cr: ContentResolver) {
    private val callLogList = mutableListOf<ItemCallLog>()

    private val projection = arrayOf(
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.DURATION
    )

    private fun getCursor() = cr.query(
        CallLog.Calls.CONTENT_URI,
        projection,
        null,
        null,
        "${CallLog.Calls.DATE} DESC" // Сортировка по дате
    )

    fun getCalls(): List<ItemCallLog> {
        getCursor()?.use {
            val idIndex = it.getColumnIndex(CallLog.Calls._ID)
            val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val number = it.getString(numberIndex)
                val name: String? = it.getString(nameIndex)
                val type = it.getInt(typeIndex)
                val date = it.getLong(dateIndex)
                val duration = it.getLong(durationIndex)

                callLogList.add(ItemCallLog(id, name, number, type.getCallType(), date, duration))
            }
        }
        return callLogList
    }

}