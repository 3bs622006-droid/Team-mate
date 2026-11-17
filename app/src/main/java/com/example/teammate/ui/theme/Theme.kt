package com.example.teammate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = DarkGreen,
    background = BackgroundGray,
    surface = CardWhite,
    onPrimary = CardWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TeamMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}