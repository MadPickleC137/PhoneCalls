package com.madpickle.calls.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
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
import com.madpickle.calls.ui.theme.navigationBar
import com.madpickle.calls.ui.theme.topAppBar

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
            modifier = Modifier
                .navigationBarsPadding()
                .systemBarsPadding()
                .imePadding(),
            backgroundColor = MaterialTheme.colors.background,
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopAppBar(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.topAppBar,
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
            content = { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding )
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigation(
                    elevation = 12.dp,
                    backgroundColor = MaterialTheme.navigationBar
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