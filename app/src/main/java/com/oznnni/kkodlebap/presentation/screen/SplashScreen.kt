package com.oznnni.kkodlebap.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oznnni.kkodlebap.R
import com.oznnni.kkodlebap.presentation.navigation.Screen
import com.oznnni.kkodlebap.presentation.util.WordPool
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme
import com.oznnni.kkodlebap.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        WordPool.getJamoParsedWords(context = context)

        delay(1500L)
        navController.navigate(Screen.Playground.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KkodlebapTheme.colors.yellow100),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = "여자아기가 꼬들밥을 땅에 내려놓는 이미지"
            )

            Text(
                modifier = Modifier.padding(top = 48.dp),
                text = "꼬들밥",
                style = Typography.SuitB1.copy(fontSize = 48.sp, lineHeight = 54.sp),
                color = KkodlebapTheme.colors.gray700
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "한글 자모 맞추기 게임",
                style = Typography.SuitM3,
                color = KkodlebapTheme.colors.gray300
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashContent() {
    SplashContent()
}
