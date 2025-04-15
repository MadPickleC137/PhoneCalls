package com.madpickle.calls.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission


@RequiresPermission(Manifest.permission.CALL_PHONE)
fun Context.makeCall(simInfo: SubscriptionInfo, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse("tel:$phoneNumber")
        putExtra("com.android.phone.extra.SIM_SLOT", simInfo.simSlotIndex) // индекс слота SIM
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    this.startActivity(intent)
}

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getSimList(): List<SubscriptionInfo>? {
    val subscriptionManager =
        this.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    return subscriptionManager.activeSubscriptionInfoList
}

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getDefaultSim(): SubscriptionInfo? {
    val telephonyManager = this.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    return telephonyManager.activeSubscriptionInfoList?.firstOrNull { it.isEmbedded }
}