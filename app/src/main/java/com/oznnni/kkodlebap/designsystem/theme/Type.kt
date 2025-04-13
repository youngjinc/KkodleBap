package com.oznnni.kkodlebap.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.oznnni.kkodlebap.R

object Typography {
    private val Suit = FontFamily(
        Font(R.font.suit_b, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.suit_r, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.suit_sb, FontWeight.SemiBold, FontStyle.Normal),
        Font(R.font.suit_m, FontWeight.Medium, FontStyle.Normal),
    )

    val SuitB1 = TextStyle(
        fontFamily = Suit,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitSb1 = TextStyle(
        fontFamily = Suit,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitM1 = TextStyle(
        fontFamily = Suit,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 34.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitM3 = TextStyle(
        fontFamily = Suit,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitM4 = TextStyle(
        fontFamily = Suit,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitR1 = TextStyle(
        fontFamily = Suit,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitR2 = TextStyle(
        fontFamily = Suit,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitR4 = TextStyle(
        fontFamily = Suit,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )

    val SuitR5 = TextStyle(
        fontFamily = Suit,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )
}

val MaterialTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
