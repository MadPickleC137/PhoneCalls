package com.madpickle.calls.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.rememberAsyncImagePainter
import com.madpickle.calls.ui.theme.HeightItem
import com.madpickle.calls.ui.theme.IconCornersShape
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.divider
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.secondaryText
import com.madpickle.calls.ui.theme.text
import java.io.File

class ContactsScreen : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val model = rememberScreenModel { ContactsModel(context.contentResolver) }
        val contacts = model.contacts.collectAsState()
        val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) model.loadContacts()
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(PaddingItem),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(PaddingItem)
        ) {
            items(contacts.value, key = { contact ->
                contact.position
            }) { item ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PaddingItem)
                ) {
                    if(item.imageUri.isNullOrEmpty()) {
                        Icon(
                            modifier = Modifier.size(HeightItem),
                            imageVector = Icons.Rounded.AccountCircle,
                            tint = MaterialTheme.icon,
                            contentDescription = ""
                        )
                    } else {
                        Box(Modifier.size(HeightItem).background(MaterialTheme.icon, IconCornersShape)) {
                            Image(
                                painter = rememberAsyncImagePainter(File(item.imageUri)),
                                contentDescription = "",
                            )
                        }
                    }
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.name, color = MaterialTheme.text)
                        Text(item.number, color = MaterialTheme.secondaryText, fontSize = 14.sp)
                        Divider(Modifier.height(1.dp), color = MaterialTheme.divider)
                    }
                }
            }
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            SideEffect {
                launcherPermission.launch(Manifest.permission.READ_CONTACTS)
            }
        } else {
            model.loadContacts()
        }
    }
}