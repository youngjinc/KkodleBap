package com.oznnni.kkodlebap.presentation.model

import java.time.LocalDateTime

data class PlaygroundUiModel(
    val input: List<JamoTile> = emptyList(),
    val keyboard: List<List<JamoTile>> = PlaygroundUiModel.keyboard,
    val tryCount: Int = 0,
    val answer: String? = null,
    val errorMessage: String = "",
    val numOfCurrentJamo: Int = 0,
    val gameClearTime: LocalDateTime? = null,
) {
    companion object {
        const val JAMO_COUNT = 6
        const val MAX_ATTEMPT_COUNT = 6
        val numOfGameBoardKey = JAMO_COUNT * MAX_ATTEMPT_COUNT
        val keyboard: List<List<JamoTile>> = listOf(
            listOf(
                JamoTile(jamo = 'ㅂ'),
                JamoTile(jamo = 'ㅈ'),
                JamoTile(jamo = 'ㄷ'),
                JamoTile(jamo = 'ㄱ'),
                JamoTile(jamo = 'ㅅ'),
                JamoTile(jamo = 'ㅛ'),
                JamoTile(jamo = 'ㅕ'),
                JamoTile(jamo = 'ㅑ'),
            ),
            listOf(
                JamoTile(jamo = 'ㅁ'),
                JamoTile(jamo = 'ㄴ'),
                JamoTile(jamo = 'ㅇ'),
                JamoTile(jamo = 'ㄹ'),
                JamoTile(jamo = 'ㅎ'),
                JamoTile(jamo = 'ㅗ'),
                JamoTile(jamo = 'ㅓ'),
                JamoTile(jamo = 'ㅏ'),
                JamoTile(jamo = 'ㅣ'),
            ),
            listOf(
                JamoTile(jamo = 'ㅋ'),
                JamoTile(jamo = 'ㅌ'),
                JamoTile(jamo = 'ㅊ'),
                JamoTile(jamo = 'ㅍ'),
                JamoTile(jamo = 'ㅠ'),
                JamoTile(jamo = 'ㅜ'),
                JamoTile(jamo = 'ㅡ'),
            )
        )
    }
}
