package com.oznnni.kkodlebap.presentation.model

import androidx.compose.ui.graphics.Color
import com.oznnni.kkodlebap.designsystem.theme.Blue100
import com.oznnni.kkodlebap.designsystem.theme.Blue400
import com.oznnni.kkodlebap.designsystem.theme.Blue600
import com.oznnni.kkodlebap.designsystem.theme.Gray200

enum class ColorType(val color: Color, val emoji: String) {
    CORRECT(color = Blue600, emoji = "💙"),
    PRESENT(color = Blue400, emoji = "🩵"),
    WRONG(color = Gray200, emoji = "🤍"),
    NONE(color = Blue100, emoji = "🤍")
}
