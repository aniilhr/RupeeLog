package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = EmeraldOnPrimaryDark,
    primaryContainer = EmeraldPrimaryContainerDark,
    onPrimaryContainer = EmeraldOnPrimaryContainerDark,
    secondary = EmeraldSecondaryDark,
    background = EmeraldBackgroundDark,
    surface = EmeraldSurfaceDark,
    surfaceVariant = EmeraldSurfaceVariantDark,
    onBackground = EmeraldOnSurfaceDark,
    onSurface = EmeraldOnSurfaceDark,
    onSurfaceVariant = EmeraldOnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimaryLight,
    onPrimary = EmeraldOnPrimaryLight,
    primaryContainer = EmeraldPrimaryContainerLight,
    onPrimaryContainer = EmeraldOnPrimaryContainerLight,
    secondary = EmeraldSecondaryLight,
    background = EmeraldBackgroundLight,
    surface = EmeraldSurfaceLight,
    surfaceVariant = EmeraldSurfaceVariantLight,
    onBackground = EmeraldOnSurfaceLight,
    onSurface = EmeraldOnSurfaceLight,
    onSurfaceVariant = EmeraldOnSurfaceVariantLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent emerald branding by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
