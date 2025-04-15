package com.madpickle.calls.dial

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import cafe.adriel.voyager.core.screen.Screen
import com.madpickle.calls.R
import com.madpickle.calls.ui.theme.dialNumber
import com.madpickle.calls.ui.theme.divider
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.simCard
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2
import com.madpickle.calls.utils.addPhoneTransformation
import com.madpickle.calls.utils.isValidPhone
import com.madpickle.calls.utils.removePhoneTransformation

class DialScreen : Screen {
    private val buttons = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"
    )

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val contentPadding = 12.dp
        val buttonShape = RoundedCornerShape(16.dp)
        var phone by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

            }
            Divider(Modifier.fillMaxWidth(), color = MaterialTheme.divider, thickness = 0.5.dp)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = phone,
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    textStyle = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.text,
                        cursorColor = MaterialTheme.text2,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {}
                )
                IconButton(
                    modifier = Modifier.padding(contentPadding),
                    onClick = {
                        phone = phone.removePhoneTransformation()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_backspace),
                        contentDescription = "",
                        tint = MaterialTheme.icon,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Divider(Modifier.fillMaxWidth(), color = MaterialTheme.divider, thickness = 0.5.dp)
            LazyVerticalGrid(
                contentPadding = PaddingValues(horizontal = 48.dp, vertical = contentPadding),
                verticalArrangement = Arrangement.spacedBy(contentPadding),
                horizontalArrangement = Arrangement.spacedBy(contentPadding),
                columns = GridCells.Fixed(3),
                userScrollEnabled = false
            ) {
                items(buttons) { item ->
                    Button(
                        modifier = Modifier.size(60.dp),
                        onClick = {
                            phone = (phone.plus(item)).addPhoneTransformation()
                        },
                        shape = buttonShape,
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 4.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.dialNumber
                        )
                    ) {
                        Text(item, fontSize = 26.sp, textAlign = TextAlign.Center)
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getSimList(context)?.forEach { info ->
                        Button(
                            onClick = {
                                makeCall(context, info, phone)
                            },
                            shape = buttonShape,
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 6.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.simCard
                            ),
                            enabled = phone.isValidPhone(),
                            modifier = Modifier.defaultMinSize(minHeight = 56.dp, minWidth = 120.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${info.displayName}", color = MaterialTheme.text)
                                Text("Sim ${info.simSlotIndex + 1}", color = MaterialTheme.text2)
                            }

                        }
                    }
                }
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
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}