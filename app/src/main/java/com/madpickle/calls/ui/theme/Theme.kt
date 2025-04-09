package com.madpickle.calls.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = Colors(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    error = Error,
    onError = OnError,
    surface = Surface,
    onSurface = OnSurface,
    primaryVariant = PrimaryVariant,
    secondaryVariant = SecondaryVariant,
    isLight = true
)

private val DarkColorScheme = Colors(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    error = ErrorDark,
    onError = OnErrorDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    primaryVariant = PrimaryVariantDark,
    secondaryVariant = SecondaryVariantDark,
    isLight = false
)

val MaterialTheme.navigationBar: Color
    @Composable
    get() = if (isSystemInDarkTheme()) NavigationDark else Background

val MaterialTheme.topAppBar: Color
    @Composable
    get() = if (isSystemInDarkTheme()) TopAppBarDark else TopAppBar

val MaterialTheme.cardItem: Color
    @Composable
    get() = if (isSystemInDarkTheme()) CallItemBackgroundDark else CallItemBackground

val MaterialTheme.text: Color
    @Composable
    get() = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary

val MaterialTheme.text2: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Text2Dark else Text2

val MaterialTheme.secondaryText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) SecondaryDark else Secondary

val MaterialTheme.divider: Color
    @Composable
    get() = if (isSystemInDarkTheme()) SecondaryDark else Secondary

val MaterialTheme.icon: Color
    @Composable
    get() = if (isSystemInDarkTheme()) SecondaryVariantDark else SecondaryVariant

@Composable
fun CallsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colors = colorScheme,
        typography = Typography,
        content = content
    )
}