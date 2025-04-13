package com.oznnni.kkodlebap.presentation.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.oznnni.kkodlebap.presentation.model.JamoTile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Context.copyTextToClipboard(count: Int, input: List<JamoTile>, gameClearTime: LocalDateTime?) {
    val text =
        "🍚 꼬들밥 🍚 ${count}회 만에 성공! ✨\n\n" +
                "${
                    input.mapIndexed { index, jamoTile -> jamoTile.colorType.emoji }.chunked(6)
                        .joinToString("\n") { it.joinToString("") }
                }\n\n" +
                "${formatKoreanDateTime(gameClearTime)}\n\n" +
                "https://play.google.com/store/apps/details?id=${this.packageName}"

    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("text", text)
    clipboard.setPrimaryClip(clip)
}

private fun formatKoreanDateTime(dateTime: LocalDateTime?): String {
    if (dateTime == null) return "\n"
    val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.KOREAN)
    val dayOfWeek = dateTime.format(dayOfWeekFormatter)

    val amPm = if (dateTime.hour < 12) "오전" else "오후"
    val hour = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
    val minute = if (dateTime.minute != 0) "${dateTime.minute}분" else ""

    return "${dateTime.year}년 ${dateTime.monthValue}월 ${dateTime.dayOfMonth}일 " +
            "(${dayOfWeek.first()}) $amPm ${hour}시 $minute"
}

