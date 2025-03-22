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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import cafe.adriel.voyager.core.screen.Screen

class CallsScreen: Screen {
    @Composable
    override fun Content() {
        val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
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