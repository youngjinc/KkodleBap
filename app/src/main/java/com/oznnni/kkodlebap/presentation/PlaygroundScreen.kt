package com.oznnni.kkodlebap.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.oznnni.kkodlebap.R
import com.oznnni.kkodlebap.designsystem.KkodlebapAlert
import com.oznnni.kkodlebap.designsystem.KkodlebapModal
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme
import com.oznnni.kkodlebap.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun PlaygroundScreen() {
    PlaygroundContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundContent() {
    val tutorialSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isOpenTutorialModal by remember { mutableStateOf(false) }
    var isOpenResultAlert by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KkodlebapTheme.colors.white),
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

        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = 56.dp)
                .fillMaxWidth(0.75f),
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(36) {
                Cell(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f), jamo = "ㅈ"
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 5.dp),
            text = "자모 6개를 입력해 주세요!",
            style = Typography.SuitR5,
            color = KkodlebapTheme.colors.red700
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ").fastForEach {
                    Key(key = it)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ").fastForEach {
                    Key(key = it)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Key(iconRes = R.drawable.ic_backspace, backgroundColor = KkodlebapTheme.colors.gray200, description = "지우기 아이콘")

                listOf("ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ").fastForEach {
                    Key(key = it)
                }

                Key(iconRes = R.drawable.ic_check, backgroundColor = KkodlebapTheme.colors.blue600, description = "정답 제출 체크 아이콘")
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    KkodlebapModal(isOpen = isOpenTutorialModal, sheetState = tutorialSheetState, onDismissRequest = { /*TODO*/ }) {
        TutorialModalContent(onClickClose = {
            coroutineScope.launch {
                tutorialSheetState.hide()
            }
            isOpenTutorialModal = false
        })
    }

    KkodlebapAlert(isOpen = isOpenResultAlert, onDismissRequest = { isOpenResultAlert = false }) {
        GameResultAlertContent(answer = "계단", gameResultRes = GameResultRes.SUCCESS)
    }
}

@Composable
private fun Cell(modifier: Modifier = Modifier, jamo: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(KkodlebapTheme.colors.blue100), contentAlignment = Alignment.Center
    ) {
        Text(
            text = jamo,
            style = Typography.SuitM1,
            color = KkodlebapTheme.colors.gray700
        )
    }
}

@Composable
private fun Key(modifier: Modifier = Modifier, key: String) {
    Box(
        modifier = modifier
            .size(width = 33.dp, height = 41.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { }
            .background(KkodlebapTheme.colors.blue100),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            style = Typography.SuitM3,
            color = KkodlebapTheme.colors.gray700
        )
    }
}

@Composable
private fun Key(modifier: Modifier = Modifier, @DrawableRes iconRes: Int, backgroundColor: Color, description: String) {
    Box(
        modifier = modifier
            .size(width = 33.dp, height = 41.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { }
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
    PlaygroundContent()
}
