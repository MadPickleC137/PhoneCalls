package com.madpickle.calls.history

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ItemCallLog
import com.madpickle.calls.domain.CallsLogContentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallsModel(
    contentResolver: ContentResolver,
    private val callsProvider: CallsLogContentProvider = CallsLogContentProvider(contentResolver)
): ScreenModel {

    private val _state = MutableStateFlow(CallsViewState())
    val viewState = _state.asStateFlow()

    fun loadCalls() {
        screenModelScope.launch {
            val calls = callsProvider.getCalls()
            _state.value = viewState.value.copy(
                loading = false,
                logs = calls
            )
        }
    }
}



data class CallsViewState(
    val loading: Boolean = true,
    val logs: List<ItemCallLog> = emptyList()
)