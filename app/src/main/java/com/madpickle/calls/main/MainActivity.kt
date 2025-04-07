package com.madpickle.calls.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.madpickle.calls.ui.theme.CallsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CallsTheme {
                window.statusBarColor = MaterialTheme.colors.surface.toArgb()
                window.navigationBarColor = MaterialTheme.colors.surface.toArgb()
                MainContent()
            }
        }
    }
}

