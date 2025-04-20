package com.madpickle.calls.detail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.madpickle.calls.ui.theme.ButtonElevation
import com.madpickle.calls.ui.theme.CardItemShape
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.simCard
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.utils.callNumberIfPossible
import com.madpickle.calls.utils.isValidPhone

@Composable
fun ItemDetailContactUI(context: Context, number: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingItem)
            .background(MaterialTheme.cardItem, CardItemShape)
            .padding(PaddingItem)
    ) {
        Text(text = number, color = MaterialTheme.text, modifier = Modifier.align(Alignment.CenterStart), fontSize = 18.sp)
        Button(
            onClick = {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    context.callNumberIfPossible(number)
                }
            },
            shape = CardItemShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.cardItem
            ),
            elevation = ButtonElevation,
            enabled = number.isValidPhone(),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterEnd)
                .background(MaterialTheme.cardItem, CardItemShape)
        ) {
            Icon(
                Icons.Rounded.Call,
                contentDescription = "",
                modifier = Modifier.size(38.dp),
                tint = MaterialTheme.simCard
            )
        }
    }
}