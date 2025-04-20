package com.madpickle.calls.data

import com.madpickle.calls.R
import com.madpickle.calls.utils.convertEpochToFormattedDate
import com.madpickle.calls.utils.convertEpochToFormattedTimeDate
import com.madpickle.calls.utils.convertEpochToFormattedTime
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

    fun getTimeDateFormatted(): String {
        return date.convertEpochToFormattedTimeDate()
    }

    fun getDateFormatted(): String {
        return date.convertEpochToFormattedDate()
    }

    fun getTimeFormatted(): String {
        return date.convertEpochToFormattedTime()
    }

    fun getIconRes(): Int {
        return when (type) {
            CallType.INCOMING -> R.drawable.ic_incom
            CallType.OUTGOING -> R.drawable.ic_outcom
            CallType.MISSING -> R.drawable.ic_missed
        }
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