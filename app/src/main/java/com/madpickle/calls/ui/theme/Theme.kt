package com.madpickle.calls.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

val MaterialTheme.text: Color
    @Composable
    get() = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary

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