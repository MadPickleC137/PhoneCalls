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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.widgets.FullProgressBar
import com.madpickle.calls.utils.grantedAll

class CallsScreen: Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val model = rememberScreenModel { CallsModel(context.contentResolver) }

        val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if(permissions.grantedAll()) {
                model.loadCalls()
            }
        }
        val vs = model.viewState.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(PaddingItem),
            verticalArrangement = Arrangement.spacedBy(PaddingItem)
        ) {
            if(vs.value.loading) {
                item {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        FullProgressBar(true)
                    }
                }
                return@LazyColumn
            }
            items(vs.value.logs) { log ->
                ItemCallLogUI(log) {
                    model.callItemClick(log)
                }
            }
        }

        SideEffect {
            launcherPermission.launch(arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            ))
        }
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private fun getSimList(context: Context): List<SubscriptionInfo>? {
        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
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