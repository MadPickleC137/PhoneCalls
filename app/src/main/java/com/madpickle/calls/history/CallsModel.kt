package com.madpickle.calls.history

import android.content.ContentResolver
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.madpickle.calls.data.ItemCallLog
import com.madpickle.calls.domain.CallsLogContentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallsModel(
    private val contentResolver: ContentResolver,
): ScreenModel {

    private val _state = MutableStateFlow(CallsViewState())
    val viewState = _state.asStateFlow()

    fun loadCalls() {
        screenModelScope.launch(Dispatchers.IO) {
            val calls = CallsLogContentProvider.getCalls(contentResolver)
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