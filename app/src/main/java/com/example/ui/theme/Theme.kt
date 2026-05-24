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
    primary = PrimaryTeal,
    secondary = SecondaryEmerald,
    tertiary = AccentPurple,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = GlowCardBackground,
    outline = SlateBorder,
    error = ErrorRed,
    onError = TextPrimary
)

private val LightColorScheme = DarkColorScheme // Keep it consistently sleek dark for the theme identity

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme by default for developer slate brand
    dynamicColor: Boolean = false, // Force consistent cyberpunk colors
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
