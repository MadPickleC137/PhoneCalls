package com.madpickle.calls.history

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.madpickle.calls.R
import com.madpickle.calls.dial.DialScreen
import com.madpickle.calls.ui.theme.MainPaddingItems
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.widgets.Fab
import com.madpickle.calls.utils.callNumberIfPossible
import com.madpickle.calls.utils.grantedAll

class CallsScreen : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.current
        val model = rememberScreenModel { CallsModel(context.contentResolver) }
        val vs = model.viewState.collectAsState()
        val launcherPermission =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.grantedAll()) {
                    model.loadCalls()
                }
            }
        SideEffect {
            launcherPermission.launch(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE
                )
            )
        }
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding =MainPaddingItems,
                verticalArrangement = Arrangement.spacedBy(PaddingItem)
            ) {
                items(vs.value.logs) { log ->
                    ItemCallLogUI(log) {
                        context.callNumberIfPossible(log.number)
                    }
                }
            }
            Fab(
                text = stringResource(R.string.keyboard_dial),
                icon = painterResource(R.drawable.ic_keyboard)
            ) {
                navigator?.push(DialScreen())
            }
        }
    }
}