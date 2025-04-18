package com.oznnni.kkodlebap.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oznnni.kkodlebap.presentation.content.GameResultRes
import com.oznnni.kkodlebap.presentation.model.ColorType
import com.oznnni.kkodlebap.presentation.model.JamoTile
import com.oznnni.kkodlebap.presentation.model.PlaygroundUiModel
import com.oznnni.kkodlebap.presentation.util.WordPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime

class PlaygroundViewModel(context: Context) : ViewModel() {
    private val _uiModel = MutableStateFlow(PlaygroundUiModel())
    val uiModel = _uiModel.asStateFlow()

    init {
        drawAnswer(context)
        WordPool.getAllWordsAsJamoList(context = context)
    }

    fun drawAnswer(context: Context) {
        _uiModel.update {
            it.copy(answer = WordPool.drawAnswer(context = context))
        }
    }

    fun submitInput(context: Context, afterSuccess: (GameResultRes) -> Unit) {
        val fromIdx = getInputFromIdx()
        val toIdx = getInputToIdx(fromIdx = fromIdx, length = uiModel.value.numOfCurrentJamo)
        val inputJamoList = uiModel.value.input.subList(fromIdx, toIdx)

        if (inputJamoList.size < PlaygroundUiModel.JAMO_COUNT) {
            _uiModel.update { it.copy(errorMessage = "자모 6개를 입력해 주세요!") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val isValid = WordPool.isValidWord(context = context, inputJamoTiles = inputJamoList)

            if (isValid) {
                val answerJamo =
                    uiModel.value.answer?.let(WordPool::splitWordToJamo) ?: return@launch
                val currentInputJamo = inputJamoList.mapIndexed { index, input ->
                    when {
                        input.jamo == answerJamo[index] -> input.copy(colorType = ColorType.CORRECT)
                        answerJamo.contains(input.jamo) -> input.copy(colorType = ColorType.PRESENT)
                        else -> input
                    }
                }

                updateKeyboard(currentGameBoardTiles = currentInputJamo)
                updateInputPartially(partialInput = currentInputJamo)
                checkGameResult(judgedInputJamoList = currentInputJamo, afterSuccess = afterSuccess)
            } else {
                clearCurrentJamoTile()
                _uiModel.update {
                    it.copy(errorMessage = "존재하지 않는 단어예요!")
                }
            }
        }
    }

    private suspend fun checkGameResult(
        judgedInputJamoList: List<JamoTile>,
        afterSuccess: (GameResultRes) -> Unit
    ) {
        val correctJamoCount = judgedInputJamoList.count { it.colorType == ColorType.CORRECT }

        when {
            correctJamoCount == PlaygroundUiModel.JAMO_COUNT -> {
                delay(200)
                _uiModel.update { it.copy(gameClearTime = LocalDateTime.now()) }
                afterSuccess(GameResultRes.SUCCESS)
            }

            uiModel.value.tryCount == PlaygroundUiModel.MAX_ATTEMPT_COUNT - 1 -> {
                delay(200)
                afterSuccess(GameResultRes.FAILURE)
            }

            else -> {
                _uiModel.update {
                    it.copy(numOfCurrentJamo = 0)
                }
            }
        }

        _uiModel.update {
            it.copy(tryCount = it.tryCount.plus(1))
        }
    }

    private fun clearCurrentJamoTile() {
        _uiModel.update { uiModel ->
            uiModel.copy(
                input = uiModel.input.dropLast(PlaygroundUiModel.JAMO_COUNT),
                numOfCurrentJamo = 0
            )
        }
    }

    private fun updateInputPartially(partialInput: List<JamoTile>) {
        var numOfUpdatedJamo = 0
        val fromIdx = getInputFromIdx()
        val toIdx = getInputToIdx(fromIdx)

        _uiModel.update { uiModel ->
            val newInput = uiModel.input.mapIndexed { idx, originTile ->
                if (idx in fromIdx until toIdx) {
                    val tile = partialInput[numOfUpdatedJamo]
                    numOfUpdatedJamo += 1
                    tile
                } else originTile
            }

            uiModel.copy(input = newInput)
        }
    }

    private fun updateKeyboard(currentGameBoardTiles: List<JamoTile>) {
        _uiModel.update {
            val newKeyboard = uiModel.value.keyboard.map { keyboardTiles ->
                keyboardTiles.map { originTile ->
                    val tile =
                        currentGameBoardTiles.find { it.jamo == originTile.jamo }

                    if (tile != null) {
                        originTile.copy(
                            colorType = if (tile.colorType != ColorType.NONE) tile.colorType
                            else ColorType.WRONG
                        )
                    } else {
                        originTile
                    }
                }
            }

            it.copy(keyboard = newKeyboard, errorMessage = "")
        }
    }

    fun onInputChanged(jamoTile: JamoTile?) {
        if (uiModel.value.errorMessage.isNotEmpty()) {
            _uiModel.update { it.copy(errorMessage = "") }
        }

        when {
            jamoTile != null && uiModel.value.numOfCurrentJamo < PlaygroundUiModel.JAMO_COUNT -> {
                _uiModel.update {
                    it.copy(
                        input = uiModel.value.input.plus(jamoTile.copy(colorType = ColorType.NONE)),
                        numOfCurrentJamo = it.numOfCurrentJamo.plus(1)
                    )
                }
            }

            jamoTile == null && uiModel.value.numOfCurrentJamo > 0 -> {
                _uiModel.update {
                    it.copy(
                        input = if (it.input.isNotEmpty()) it.input.dropLast(1) else it.input,
                        numOfCurrentJamo = it.numOfCurrentJamo.minus(1)
                    )
                }
            }
        }
    }

    fun clearGame() {
        _uiModel.update { PlaygroundUiModel() }
    }

    private fun getInputFromIdx() = uiModel.value.tryCount * PlaygroundUiModel.JAMO_COUNT

    private fun getInputToIdx(fromIdx: Int, length: Int = PlaygroundUiModel.JAMO_COUNT) =
        fromIdx + length
}
