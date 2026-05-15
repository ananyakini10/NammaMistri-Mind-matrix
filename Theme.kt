package com.nammamistri.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF135D66),
    secondary = Color(0xFFE15A3D),
    tertiary = Color(0xFFFFC857),
    background = Color(0xFFF7F6F2),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF222222),
    onBackground = Color(0xFF1E2526),
    onSurface = Color(0xFF1E2526)
)

@Composable
fun NammaMistriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
