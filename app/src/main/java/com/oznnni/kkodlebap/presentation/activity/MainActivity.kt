package com.oznnni.kkodlebap.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oznnni.kkodlebap.presentation.navigation.MyAppNavHost
import com.oznnni.kkodlebap.designsystem.theme.KkodlebapTheme
import com.oznnni.kkodlebap.designsystem.theme.Yellow100

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Yellow100,
                    darkIcons = true,
                )
            }

            KkodlebapTheme {
                MyAppNavHost()
            }
        }
    }
}
