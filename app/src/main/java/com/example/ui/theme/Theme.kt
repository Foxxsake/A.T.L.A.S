package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = AtlasGold,
    onPrimary = Color.Black,
    secondary = AtlasGoldDark,
    onSecondary = Color.Black,
    tertiary = AtlasGreen,
    background = AtlasBackground,
    onBackground = Color.White,
    surface = AtlasSurface,
    onSurface = Color.White,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content,
  )
}

