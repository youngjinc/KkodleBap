package com.oznnni.kkodlebap.presentation.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oznnni.kkodlebap.presentation.content.GameResultRes
import com.oznnni.kkodlebap.ui.theme.Blue100
import com.oznnni.kkodlebap.ui.theme.Blue400
import com.oznnni.kkodlebap.ui.theme.Blue600
import com.oznnni.kkodlebap.ui.theme.Gray200
import com.oznnni.kkodlebap.util.WordPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ColorType(val color: Color, val emoji: String) {
    CORRECT(color = Blue600, emoji = "💙"),
    PRESENT(color = Blue400, emoji = "🩵"),
    WRONG(color = Gray200, emoji = "🤍"),
    NONE(color = Blue100, emoji = "🤍")
}

data class JamoTile(
    val jamo: Char? = ' ',
    val colorType: ColorType = ColorType.NONE,
)

data class PlaygroundUiModel(
    val input: List<JamoTile> = emptyList(),
    val keyboard: List<List<JamoTile>> = PlaygroundUiModel.keyboard,
    val tryCount: Int = 0,
    val answer: String? = null,
    val errorMessage: String = "",
    val numOfCurrentJamo: Int = 0,
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

class PlaygroundViewModel : ViewModel() {
    private val _uiModel = MutableStateFlow(PlaygroundUiModel())
    val uiModel = _uiModel.asStateFlow()

    fun drawAnswer(context: Context) {
        _uiModel.update {
            it.copy(answer = WordPool.getRandomWord(context = context))
        }
        Timber.e("answer: ${uiModel.value.answer}")
    }

    fun submitInput(context: Context, afterSuccess: (GameResultRes) -> Unit) {
        val fromIdx = getInputFromIdx()
        val toIdx = getInputToIdx(fromIdx)
        val inputJamoList = uiModel.value.input.subList(fromIdx, toIdx)

        if (inputJamoList.size < PlaygroundUiModel.JAMO_COUNT) {
            _uiModel.update {
                it.copy(errorMessage = "자모 6개를 입력해 주세요!")
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (WordPool.isExistingWord(context = context, inputJamoTiles = inputJamoList)) {
                val answerJamoList =
                    uiModel.value.answer?.let(WordPool::splitWordToJamo) ?: return@launch
                val updatedPartialJamoTile = inputJamoList.mapIndexed { index, inputJamo ->
                    when {
                        inputJamo.jamo == answerJamoList[index] -> inputJamo.copy(colorType = ColorType.CORRECT)
                        answerJamoList.contains(inputJamo.jamo) -> inputJamo.copy(colorType = ColorType.PRESENT)
                        else -> inputJamo
                    }
                }

                updateKeyboard(currentGameBoardTiles = updatedPartialJamoTile)
                updateJamoTile(updatedPartialJamoTile = updatedPartialJamoTile)

                Timber.e(uiModel.value.tryCount.toString())
                if (updatedPartialJamoTile.count { it.colorType == ColorType.CORRECT } == PlaygroundUiModel.JAMO_COUNT) {
                    afterSuccess(GameResultRes.SUCCESS)
                } else if (uiModel.value.tryCount == PlaygroundUiModel.MAX_ATTEMPT_COUNT - 1) {
                    afterSuccess(GameResultRes.FAILURE)
                } else {
                    _uiModel.update {
                        it.copy(numOfCurrentJamo = 0, tryCount = it.tryCount.plus(1))
                    }
                }
            } else {
                clearCurrentJamoTile() // TODO 메세지 띄우기
                _uiModel.update {
                    it.copy(errorMessage = "존재하지 않는 단어예요!")
                }
            }
        }
    }

    private fun clearCurrentJamoTile() {
        Timber.e("hello4")
        _uiModel.update { uiModel ->
            uiModel.copy(
                input = uiModel.input.dropLast(PlaygroundUiModel.JAMO_COUNT),
                numOfCurrentJamo = 0
            )
        }
    }

    private fun updateJamoTile(updatedPartialJamoTile: List<JamoTile>) {
        Timber.e("hello5")
        var updatedJamoCount = 0
        val fromIdx = getInputFromIdx()
        val toIdx = getInputToIdx(fromIdx)

        _uiModel.update { uiModel ->
            val newInput = uiModel.input.mapIndexed { idx, jamoTile ->
                if (idx in fromIdx until toIdx) {
                    val tile = updatedPartialJamoTile[updatedJamoCount]
                    updatedJamoCount += 1
                    tile
                } else jamoTile
            }

            uiModel.copy(input = newInput)
        }
    }

    private fun updateKeyboard(currentGameBoardTiles: List<JamoTile>) {
        Timber.e("updatedPartialJamoTile :$currentGameBoardTiles")
        _uiModel.update {
            it.copy(keyboard = uiModel.value.keyboard.map { keyboardTiles ->
                keyboardTiles.map { keyboardTile ->
                    val gameBoardJamoTile =
                        currentGameBoardTiles.find { it.jamo == keyboardTile.jamo }

                    if (gameBoardJamoTile != null) {
                        keyboardTile.copy(
                            colorType = if (gameBoardJamoTile.colorType != ColorType.NONE) gameBoardJamoTile.colorType
                            else ColorType.WRONG
                        )
                    } else {
                        keyboardTile
                    }
                }
            }, errorMessage = "")
        }

    }

    fun onChangeInput(jamoTile: JamoTile?) {
        if (uiModel.value.errorMessage.isNotEmpty()) {
            _uiModel.update { it.copy(errorMessage = "") }
        }
        Timber.e("hello7")
        Timber.e("jamoTile: $jamoTile")
        if (jamoTile != null && uiModel.value.numOfCurrentJamo < PlaygroundUiModel.JAMO_COUNT) {
            Timber.e("case1 : ${uiModel.value.input.plus(jamoTile)}")
            _uiModel.update {
                it.copy(
                    input = uiModel.value.input.plus(jamoTile.copy(colorType = ColorType.NONE)),
                    numOfCurrentJamo = it.numOfCurrentJamo.plus(1)
                )
            }
            Timber.e("uimodel: ${uiModel.value}")
        } else if (jamoTile == null && uiModel.value.numOfCurrentJamo > 0) {
            Timber.e("case2 ${if (uiModel.value.input.isNotEmpty()) uiModel.value.input.dropLast(1) else uiModel.value.input}")
            _uiModel.update {
                it.copy(
                    input = if (it.input.isNotEmpty()) it.input.dropLast(1) else it.input,
                    numOfCurrentJamo = it.numOfCurrentJamo.minus(1)
                )
            }
        }
    }

    fun clearGame() {
        Timber.e("hello8")
        _uiModel.update { PlaygroundUiModel() }
    }

    private fun getInputFromIdx() = uiModel.value.tryCount * PlaygroundUiModel.JAMO_COUNT

    private fun getInputToIdx(fromIdx: Int) = fromIdx + PlaygroundUiModel.JAMO_COUNT
}
