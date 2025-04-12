package com.oznnni.kkodlebap.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oznnni.kkodlebap.R
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme
import com.oznnni.kkodlebap.ui.theme.Typography

@Composable
fun GameResultAlertContent(answer: String, gameResultRes: GameResultRes) {
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
            onClick = { /** TODO */ },
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
                    .clickable { /** TODO */}
                    .padding(10.dp),
                text = "결과 공유하기",
                style = Typography.SuitM4,
                color = KkodlebapTheme.colors.gray400
            )
        }
        else {
            Spacer(modifier = Modifier.height(24.dp))
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
    GameResultAlertContent(answer = "계단", gameResultRes = GameResultRes.SUCCESS)
}