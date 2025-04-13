package com.oznnni.kkodlebap.presentation.util

import android.content.Context
import com.oznnni.kkodlebap.presentation.viewmodel.JamoTile
import com.oznnni.kkodlebap.presentation.viewmodel.PlaygroundUiModel

object WordPool {
    private var answerWords = emptyList<String>()
    private var allWords = emptyList<String>()
    private var jamoParsedWords = emptyList<List<Char>>()

    private const val CHO = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"
    private const val JUNG = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ"
    private val JONG = listOf(
        "", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ",
        "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ",
        "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    )

    private val complexInitialMap = mapOf(
        'ㄲ' to listOf('ㄱ', 'ㄱ'), 'ㄸ' to listOf('ㄷ', 'ㄷ'),
        'ㅃ' to listOf('ㅂ', 'ㅂ'), 'ㅆ' to listOf('ㅅ', 'ㅅ'), 'ㅉ' to listOf('ㅈ', 'ㅈ')
    )

    private val complexMedialMap = mapOf(
        'ㅘ' to listOf('ㅗ', 'ㅏ'), 'ㅙ' to listOf('ㅗ', 'ㅐ'), 'ㅚ' to listOf('ㅗ', 'ㅣ'),
        'ㅝ' to listOf('ㅜ', 'ㅓ'), 'ㅞ' to listOf('ㅜ', 'ㅔ'), 'ㅟ' to listOf('ㅜ', 'ㅣ'),
        'ㅢ' to listOf('ㅡ', 'ㅣ'), 'ㅐ' to listOf('ㅏ', 'ㅣ'), 'ㅔ' to listOf('ㅓ', 'ㅣ'),
        'ㅒ' to listOf('ㅑ', 'ㅣ'), 'ㅖ' to listOf('ㅕ', 'ㅣ'),
    )

    private val complexFinalMap = mapOf(
        "ㄳ" to listOf('ㄱ', 'ㅅ'), "ㄵ" to listOf('ㄴ', 'ㅈ'), "ㄶ" to listOf('ㄴ', 'ㅎ'),
        "ㄺ" to listOf('ㄹ', 'ㄱ'), "ㄻ" to listOf('ㄹ', 'ㅁ'), "ㄼ" to listOf('ㄹ', 'ㅂ'),
        "ㄽ" to listOf('ㄹ', 'ㅅ'), "ㄾ" to listOf('ㄹ', 'ㅌ'), "ㄿ" to listOf('ㄹ', 'ㅍ'),
        "ㅀ" to listOf('ㄹ', 'ㅎ'), "ㅄ" to listOf('ㅂ', 'ㅅ'),
        "ㄲ" to listOf('ㄱ', 'ㄱ'), "ㅆ" to listOf('ㅅ', 'ㅅ')
    )

    private fun getAnswerWords(context: Context): List<String> {
        if (answerWords.isEmpty()) {
            answerWords =
                context.assets.open("common_nouns.txt").bufferedReader().useLines { lines ->
                    lines.map { it.trim() }
                        .filter { it.isNotEmpty() && splitWordToJamo(it).size == PlaygroundUiModel.JAMO_COUNT }
                        .toList()
                }
        }
        return answerWords
    }

    private fun getAllWords(context: Context): List<String> {
        if (allWords.isEmpty()) {
            allWords = context.assets.open("all_nouns.txt").bufferedReader().useLines { lines ->
                lines.map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toList()
            }
        }

        return allWords
    }

    fun getJamoParsedWords(context: Context): List<List<Char>> {
        if (jamoParsedWords.isEmpty()) {
            jamoParsedWords = getAllWords(context = context).map { splitWordToJamo(it) }
        }

        return jamoParsedWords
    }

    fun getRandomWord(context: Context): String? {
        val wordList = getAnswerWords(context)
        val word = wordList.randomOrNull() ?: return null
        answerWords = answerWords.minus(word)
        return word
    }

    fun isExistingWord(context: Context, inputJamoTiles: List<JamoTile>): Boolean {
        return getJamoParsedWords(context).contains(inputJamoTiles.map { it.jamo })
    }

    fun splitWordToJamo(input: String): List<Char> {
        val result = mutableListOf<Char>()

        for (char in input) {
            if (char in '가'..'힣') {
                val code = char.code - 0xAC00
                val choIdx = code / (21 * 28)
                val jungIdx = (code % (21 * 28)) / 28
                val jongIdx = code % 28

                val cho = CHO[choIdx]
                val jung = JUNG[jungIdx]
                val jong = JONG[jongIdx]

                // 초성 분해
                result += complexInitialMap[cho] ?: listOf(cho)
                // 중성 분해
                result += complexMedialMap[jung] ?: listOf(jung)
                // 종성 분해
                if (jong.isNotEmpty()) {
                    result += complexFinalMap[jong] ?: listOf(jong[0])
                }
            } else {
                result += char
            }
        }

        return result
    }
}
