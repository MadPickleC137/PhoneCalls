package com.madpickle.calls.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.madpickle.calls.contacts.ContactsTab
import com.madpickle.calls.history.CallsTab

@Composable
fun MainContent() {
    TabNavigator(
        CallsTab,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = listOf(CallsTab, ContactsTab)
            )
        }
    ) { tabNavigator ->
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopAppBar(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = "", tint = MaterialTheme.colors.primary)
                        }
                    },
                    title = {
                        Text(
                            text = tabNavigator.current.options.title,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary
                        )
                    }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigation(
                    elevation = 12.dp,
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    TabNavigationItem(CallsTab)
                    TabNavigationItem(ContactsTab)
                }
            }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = MaterialTheme.colors.secondary,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title, fontSize = 10.sp) },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title
            )
        }
    )
}