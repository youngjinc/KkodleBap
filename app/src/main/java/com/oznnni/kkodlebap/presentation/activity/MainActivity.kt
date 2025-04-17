package com.oznnni.kkodlebap.presentation.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.oznnni.kkodlebap.designsystem.theme.KkodlebapTheme
import com.oznnni.kkodlebap.designsystem.theme.Yellow100
import com.oznnni.kkodlebap.presentation.navigation.MyAppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
