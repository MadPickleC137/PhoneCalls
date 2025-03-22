package com.madpickle.calls.data


data class ItemCallLog(
    val id: String,
    val number: String,
    val type: CallType,
    val date: Long,
    val duration: Long
)

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