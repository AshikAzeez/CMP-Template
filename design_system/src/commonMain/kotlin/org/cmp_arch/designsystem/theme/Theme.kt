package org.cmp_arch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.cmp_arch.designsystem.system.ConfigureSystemBars

private val LightScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF1456F0),
    onPrimary = Color.White,
    secondary = Color(0xFF0E9F6E),
    onSecondary = Color.White,
    surface = Color(0xFFF6F8FC),
    onSurface = Color(0xFF111827),
    background = Color(0xFFF0F4FA),
    onBackground = Color(0xFF111827),
)

private val DarkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFF8CABFF),
    onPrimary = Color(0xFF002978),
    secondary = Color(0xFF5DE6B0),
    onSecondary = Color(0xFF003723),
    surface = Color(0xFF10141E),
    onSurface = Color(0xFFECF0F8),
    background = Color(0xFF090C14),
    onBackground = Color(0xFFE6EAF2),
)

@Composable
fun CmpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    ConfigureSystemBars(darkTheme = darkTheme)

    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = Typography(),
        content = content,
    )
}
