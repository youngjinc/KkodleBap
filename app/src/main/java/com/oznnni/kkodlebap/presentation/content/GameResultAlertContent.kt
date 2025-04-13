package com.oznnni.kkodlebap.presentation.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oznnni.kkodlebap.R
import com.oznnni.kkodlebap.designsystem.KkodlebapSnackbar
import com.oznnni.kkodlebap.designsystem.KkodlebapSnackbarHost
import com.oznnni.kkodlebap.designsystem.showKkodelbapSnackbar
import com.oznnni.kkodlebap.designsystem.toMillis
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme
import com.oznnni.kkodlebap.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameResultAlertContent(
    coroutineScope: CoroutineScope,
    answer: String,
    gameResultRes: GameResultRes,
    onClick: () -> Unit,
    onClickShare: () -> Unit,
) {
    var snackbarData by remember { mutableStateOf<String?>(null) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 42.dp),
                text = gameResultRes.title,
                style = Typography.SuitB1,
                color = KkodlebapTheme.colors.gray700
            )
            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = "정답은 ‘$answer’ 입니다!",
                style = Typography.SuitSb1,
                color = KkodlebapTheme.colors.gray700
            )
            Image(
                modifier = Modifier.padding(top = 48.dp),
                painter = painterResource(id = gameResultRes.imageRes),
                contentDescription = gameResultRes.imageDescription
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = gameResultRes.description,
                style = Typography.SuitR5,
                color = KkodlebapTheme.colors.gray300
            )

            Button(
                modifier = Modifier
                    .padding(top = 68.dp)
                    .fillMaxWidth(),
                onClick = {
                    onClick()
                },
                shape = RoundedCornerShape(10.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    contentColor = KkodlebapTheme.colors.white,
                    containerColor = KkodlebapTheme.colors.blue600
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Text(
                    text = gameResultRes.buttonText,
                    style = Typography.SuitM3,
                )
            }

            if (gameResultRes == GameResultRes.SUCCESS) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 14.dp)
                        .clickable {
                            coroutineScope.launch {
                                showKkodelbapSnackbar(
                                    message = "복사된 결과를 공유해 주세요 🍚",
                                    snackbarDuration = SnackbarDuration.Short,
                                    updateSnackbarData = { data ->
                                        snackbarData = data
                                    }
                                )
                                onClickShare()
                            }
                        }
                        .padding(10.dp),
                    text = "결과 공유하기",
                    style = Typography.SuitM4,
                    color = KkodlebapTheme.colors.gray400
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }


        /*
        Snackbar를 커스텀한 이유:
        Dialog 내부에서 커스텀 스낵바를 노출시켜 Dialog 위로 스낵바를 노출시키기 위함
        SnackbarHost는 Dialog 외부 레이어이기 때문에 Dialog 위로 스낵바를 노출시킬 수 없음.
        */
        KkodlebapSnackbarHost(
            current = snackbarData,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { data ->
            KkodlebapSnackbar(
                message = data,
            )
        }
    }
}

enum class GameResultRes(
    val title: String,
    @DrawableRes val imageRes: Int,
    val imageDescription: String,
    val description: String,
    val buttonText: String
) {
    SUCCESS(
        title = "🥳 축하합니다 🥳",
        imageRes = R.drawable.img_success,
        imageDescription = "여자아기가 밥풀을 잡고 있는 이미지",
        description = "밥풀을 모은 꼬들이는 행복해요!",
        buttonText = "한 판 더!"
    ),
    FAILURE(
        title = "아쉽네요.. 🥲",
        imageRes = R.drawable.img_failure,
        imageDescription = "빈 밥그릇 이미지",
        description = "텅 - 다시 한번 해볼까요?",
        buttonText = "다시 도전하기"
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewGameResultAlertContent() {
    GameResultAlertContent(
        coroutineScope = rememberCoroutineScope(),
        answer = "계단",
        gameResultRes = GameResultRes.SUCCESS,
        onClick = {},
        onClickShare = {}
    )
}