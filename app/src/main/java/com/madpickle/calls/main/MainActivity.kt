package com.madpickle.calls.main

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.madpickle.calls.R
import com.madpickle.calls.ui.theme.CallsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallsTheme {
                setSystemBarsColor()
                MainContent()
            }
        }
    }

    private fun setSystemBarsColor() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigation_bar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

