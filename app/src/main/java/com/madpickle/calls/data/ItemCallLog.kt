package com.madpickle.calls.data

import com.madpickle.calls.utils.convertEpochToFormattedDate
import com.madpickle.calls.utils.convertToMMSS


data class ItemCallLog(
    val id: String,
    val name: String?,
    val number: String,
    val type: CallType,
    val date: Long,
    val duration: Long
) {

    fun getDurationToMMSS(): String {
        return duration.convertToMMSS()
    }

    fun getDateFormatted(): String {
        return date.convertEpochToFormattedDate()
    }
}

enum class CallType {
    INCOMING, OUTGOING, MISSING
}

fun Int?.getCallType(): CallType {
    if(this == null) CallType.MISSING
    return when(this) {
        1 -> CallType.INCOMING
        2 -> CallType.OUTGOING
        3 -> CallType.MISSING
        else -> CallType.MISSING
    }
}