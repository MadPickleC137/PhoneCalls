package com.madpickle.calls.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.madpickle.calls.R
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.widgets.Fab

class ContactsScreen : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val model = rememberScreenModel { ContactsModel(context.contentResolver) }
        val contacts = model.contacts.collectAsState()
        val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) model.loadContacts()
        }
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(PaddingItem),
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(PaddingItem)
            ) {
                items(contacts.value, key = { contact ->
                    contact.position
                }) { item ->
                    ItemContactUI(item) { }
                }
            }
            Fab(
                text = stringResource(R.string.add),
                icon = painterResource(R.drawable.ic_add)
            ) { }
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