package com.dmm.bootcamp.yatter2023.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val darkColorScheme = darkColors(
  primary = Purple80,
  secondary = PurpleGrey80,
)

private val lightColorScheme = lightColors(
  primary = Purple40,
  secondary = PurpleGrey40,
)

@Composable
fun Yatter2023Theme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme =
    if (darkTheme) darkColorScheme else lightColorScheme

  MaterialTheme(
    colors = colorScheme,
    typography = Typography,
    content = content
  )
}