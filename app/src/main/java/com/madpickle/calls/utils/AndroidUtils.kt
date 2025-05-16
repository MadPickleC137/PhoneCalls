package com.madpickle.calls.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat


fun Context.isAllGranted(): Boolean {
    return arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.GET_ACCOUNTS,
        Manifest.permission.CALL_PHONE
    ).all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}



fun Context.callNumberIfPossible(number: String) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        this.getDefaultSim()?.let {
            this.makeCall(it, number)
        }
    }
}

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
fun Context.getSimList(): List<SubscriptionInfo> {
    val subscriptionManager =
        this.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    return subscriptionManager.activeSubscriptionInfoList ?: emptyList()
}

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getDefaultSim(): SubscriptionInfo? {
    val subscriptionManager = this.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
    val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    // Получаем список всех доступных SIM-карт
    val subscriptionList = subscriptionManager?.activeSubscriptionInfoList

    // Получаем поулчаем id выбранной сим-карты
    val simCarrierId = telephonyManager?.simCarrierId

    // Ищем SIM-карту, которая используется для звонков
    subscriptionList?.forEach {
        if(it.carrierId == simCarrierId) {
            return it
        }
    }
    return null // Если не нашли подходящую SIM-карту
}


fun Bitmap.getResizedBitmap(
    newWidth: Int,
    newHeight: Int,
    keepOriginal: Boolean = true
): Bitmap {
    val width = this.width
    val height = this.height
    val scaleWidth = (newWidth.toFloat()) / width
    val scaleHeight = (newHeight.toFloat()) / height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    val resizedBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    if (!keepOriginal) {
        this.recycle()
    }
    return resizedBitmap
}