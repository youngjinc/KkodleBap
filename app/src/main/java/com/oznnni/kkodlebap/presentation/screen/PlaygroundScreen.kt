package com.oznnni.kkodlebap.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oznnni.kkodlebap.R
import com.oznnni.kkodlebap.designsystem.KkodlebapAlert
import com.oznnni.kkodlebap.designsystem.KkodlebapModal
import com.oznnni.kkodlebap.designsystem.theme.KkodlebapTheme
import com.oznnni.kkodlebap.designsystem.theme.Typography
import com.oznnni.kkodlebap.presentation.content.GameResultAlertContent
import com.oznnni.kkodlebap.presentation.content.GameResultRes
import com.oznnni.kkodlebap.presentation.content.TutorialModalContent
import com.oznnni.kkodlebap.presentation.model.JamoTile
import com.oznnni.kkodlebap.presentation.model.PlaygroundUiModel
import com.oznnni.kkodlebap.presentation.util.copyTextToClipboard
import com.oznnni.kkodlebap.presentation.viewmodel.KkodlebapViewModelFactory
import com.oznnni.kkodlebap.presentation.viewmodel.PlaygroundViewModel
import kotlinx.coroutines.launch

@Composable
fun PlaygroundScreen() {
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val viewModel =
        viewModel<PlaygroundViewModel>(factory = KkodlebapViewModelFactory(context.applicationContext))
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    var gameResultRes by remember { mutableStateOf<GameResultRes?>(null) }

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true,
        )
    }

    PlaygroundContent(
        uiModel = uiModel,
        gameResultRes = gameResultRes,
        updateGameResultRes = {
            gameResultRes = it
        },
        onClickJamoKey = {
            viewModel.onInputChanged(jamoTile = it)
        },
        onClickSubmit = {
            viewModel.submitInput(context = context, afterSuccess = {
                gameResultRes = it
            })
        },
        onClickDelete = {
            viewModel.onInputChanged(jamoTile = null)
        },
        replayGame = {
            viewModel.clearGame()
            viewModel.drawAnswer(context = context)
        },
        onClickShare = {
            context.copyTextToClipboard(
                count = uiModel.tryCount,
                input = uiModel.input,
                gameClearTime = uiModel.gameClearTime
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundContent(
    uiModel: PlaygroundUiModel,
    gameResultRes: GameResultRes?,
    updateGameResultRes: (GameResultRes?) -> Unit,
    onClickJamoKey: (JamoTile) -> Unit,
    onClickSubmit: () -> Unit,
    onClickDelete: () -> Unit,
    replayGame: () -> Unit,
    onClickShare: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val tutorialSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isOpenTutorialModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KkodlebapTheme.colors.white)
            .padding(bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(width = 65.dp, height = 50.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_kkodlebap_logo),
                contentDescription = "꼬들밥 로고"
            )

            Box(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp)
                .clip(CircleShape)
                .clickable {
                    coroutineScope.launch {
                        tutorialSheetState.show()
                    }
                    isOpenTutorialModal = true
                }
                .size(48.dp), contentAlignment = Alignment.Center) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_question_mark),
                    contentDescription = "도움말",
                    tint = Color.Unspecified
                )
            }
        }

        GameBoard(input = uiModel.input)

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 5.dp),
            text = uiModel.errorMessage,
            style = Typography.SuitR5,
            color = KkodlebapTheme.colors.red700
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            KeyBoard(
                keyboard = uiModel.keyboard,
                onClickJamoKey = onClickJamoKey,
                onClickSubmit = onClickSubmit,
                onClickDelete = onClickDelete
            )
        }
    }

    KkodlebapAlert(
        isOpen = gameResultRes != null,
        onDismissRequest = {
            replayGame()
            updateGameResultRes(null)
        }
    ) {
        gameResultRes?.let {
            GameResultAlertContent(
                coroutineScope = coroutineScope,
                answer = uiModel.answer ?: "",
                gameResultRes = it,
                onClick = {
                    replayGame()
                    updateGameResultRes(null)
                },
                onClickShare = onClickShare,
            )
        }
    }

    KkodlebapModal(
        isOpen = isOpenTutorialModal,
        sheetState = tutorialSheetState,
        onDismissRequest = {
            isOpenTutorialModal = false
        }
    ) {
        TutorialModalContent(
            onClickClose = {
                coroutineScope.launch {
                    tutorialSheetState.hide()
                }
                isOpenTutorialModal = false
            }
        )
    }
}

@Composable
private fun ColumnScope.GameBoard(input: List<JamoTile>) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 56.dp)
            .fillMaxWidth(0.75f),
        columns = GridCells.Fixed(6),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(PlaygroundUiModel.numOfGameBoardKey) { idx ->
            if (idx < input.size) {
                GameBoardCell(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    jamoTile = input[idx],
                )
            } else {
                GameBoardCell(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    jamoTile = JamoTile(),
                )
            }
        }
    }
}

@Composable
private fun GameBoardCell(modifier: Modifier = Modifier, jamoTile: JamoTile) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(jamoTile.colorType.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = jamoTile.jamo.toString(),
            style = Typography.SuitM1,
            color = KkodlebapTheme.colors.gray700
        )
    }
}

@Composable
private fun KeyBoard(
    keyboard: List<List<JamoTile>>,
    onClickJamoKey: (JamoTile) -> Unit,
    onClickSubmit: () -> Unit,
    onClickDelete: () -> Unit
) {
    keyboard.fastForEachIndexed { i, jamoTiles ->
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            when (i) {
                keyboard.lastIndex -> {
                    IconKey(
                        iconRes = R.drawable.ic_backspace,
                        backgroundColor = KkodlebapTheme.colors.gray200,
                        description = "지우기 아이콘",
                        onClick = onClickDelete

                    )

                    jamoTiles.fastForEach { jamoTile ->
                        JamoKey(jamoTile = jamoTile, onClickJamoKey = onClickJamoKey)
                    }

                    IconKey(
                        iconRes = R.drawable.ic_check,
                        backgroundColor = KkodlebapTheme.colors.blue600,
                        description = "정답 제출 체크 아이콘",
                        onClick = onClickSubmit
                    )
                }

                else -> {
                    jamoTiles.fastForEach { jamoTile ->
                        JamoKey(jamoTile = jamoTile, onClickJamoKey = onClickJamoKey)
                    }
                }
            }
        }
    }
}

@Composable
private fun JamoKey(
    modifier: Modifier = Modifier,
    jamoTile: JamoTile,
    onClickJamoKey: (JamoTile) -> Unit
) {
    Box(
        modifier = modifier
            .size(width = 33.dp, height = 41.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                onClickJamoKey(jamoTile)
            }
            .background(jamoTile.colorType.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = jamoTile.jamo.toString(),
            style = Typography.SuitM3,
            color = KkodlebapTheme.colors.gray700
        )
    }
}

@Composable
private fun IconKey(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    description: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(width = 33.dp, height = 41.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                onClick()
            }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            tint = Color.Unspecified,
            contentDescription = description
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlaygroundContent() {
    PlaygroundContent(
        uiModel = PlaygroundUiModel(),
        gameResultRes = null,
        updateGameResultRes = {},
        onClickJamoKey = {},
        onClickSubmit = {},
        onClickDelete = {},
        replayGame = {},
        onClickShare = {},
    )
}
