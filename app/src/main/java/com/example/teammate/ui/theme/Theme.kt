package com.example.teammate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = DarkGreen,
    primaryContainer = LightGreenBg,
    onPrimaryContainer = DarkGreen,
    background = BackgroundGray,
    surface = CardWhite,
    surfaceVariant = SurfaceVariantLight,
    onPrimary = TextOnPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    outlineVariant = DividerGray,
    error = StatusErrorText,
    errorContainer = StatusErrorBg
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