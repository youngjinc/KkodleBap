package com.oznnni.kkodlebap.designsystem.theme

import androidx.compose.ui.graphics.Color

// Gray Scale
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Gray100 = Color(0xFFF2F2F7)
val Gray200 = Color(0xFFD1D1D6)
val Gray300 = Color(0xFFB1B1B5)
val Gray400 = Color(0xFF8E8E93)
val Gray700 = Color(0xFF292929)

// Blue Scale
val Blue100 = Color(0xFFF2F8FF)
val Blue400 = Color(0xFF96E3FB)
val Blue600 = Color(0xFF3394FF)

// Red Scale
val Red700 = Color(0xFFFF4C42)

// Yellow Scale
val Yellow100 = Color(0xFFFFFEF2)

// System
val BaseRipple = Color(0x42000000)

data class KkoddlebapColors(
    val white: Color,
    val black: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray700: Color,
    val blue100: Color,
    val blue400: Color,
    val blue600: Color,
    val red700: Color,
    val yellow100: Color,
    val baseRipple: Color,
)

fun kkodlebapLightColors() = KkoddlebapColors(
    white = White,
    black = Black,
    gray100 = Gray100,
    gray200 = Gray200,
    gray300 = Gray300,
    gray400 = Gray400,
    gray700 = Gray700,
    blue100 = Blue100,
    blue400 = Blue400,
    blue600 = Blue600,
    red700 = Red700,
    yellow100 = Yellow100,
    baseRipple = BaseRipple,
)