package com.oznnni.kkodlebap.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = White,
    secondary = Blue600,
    tertiary = Blue400
)

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    secondary = Blue400,
    tertiary = Gray700,
    background = White,
    onBackground = White,
    surface = White,
    onSurface = White,
)

private val LocalColors = staticCompositionLocalOf { kkodlebapLightColors() }

@Composable
fun KkodlebapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: KkoddlebapColors = LocalColors.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = MaterialTypography,
            content = content,
        )
    }
}

object KkodlebapTheme {
    val colors: KkoddlebapColors
        @Composable
        get() = LocalColors.current
}