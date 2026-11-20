
package com.rparchitect.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RPColorScheme = darkColorScheme(
    primary = RPArchitectColors.PrimaryText,
    onPrimary = RPArchitectColors.Background,
    secondary = RPArchitectColors.SecondaryText,
    onSecondary = RPArchitectColors.Background,
    background = RPArchitectColors.Background,
    onBackground = RPArchitectColors.PrimaryText,
    surface = RPArchitectColors.Surface,
    onSurface = RPArchitectColors.PrimaryText,
    surfaceVariant = RPArchitectColors.IconBackground,
    onSurfaceVariant = RPArchitectColors.SecondaryText,
    outline = RPArchitectColors.Outline,
    error = RPArchitectColors.Error
)

@Composable
fun RPArchitectTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RPColorScheme,
        content = content
    )
}
