package com.madpickle.calls.history

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.madpickle.calls.R
import com.madpickle.calls.ui.theme.FabShape
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.fab
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2
import com.madpickle.calls.ui.theme.widgets.Fab
import com.madpickle.calls.ui.theme.widgets.FullProgressBar
import com.madpickle.calls.utils.grantedAll

class CallsScreen : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
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
        if (vs.value.loading) {
            Box(
                Modifier.fillMaxSize()
            ) {
                FullProgressBar(true)
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(PaddingItem),
                    verticalArrangement = Arrangement.spacedBy(PaddingItem)
                ) {
                    items(vs.value.logs) { log ->
                        ItemCallLogUI(log) {
                            model.callItemClick(log)
                        }
                    }
                }
                Fab(
                    text = stringResource(R.string.keyboard_dial),
                    icon = painterResource(R.drawable.ic_keyboard)
                ) { }
            }
        }
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private fun getSimList(context: Context): List<SubscriptionInfo>? {
        val subscriptionManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        return subscriptionManager.activeSubscriptionInfoList
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    private fun makeCall(context: Context, simInfo: SubscriptionInfo, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            putExtra("com.android.phone.extra.SIM_SLOT", simInfo.simSlotIndex) // индекс слота SIM
        }
        context.startActivity(intent)
    }
}