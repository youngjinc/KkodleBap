package com.oznnni.kkodlebap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oznnni.kkodlebap.presentation.screen.PlaygroundScreen
import com.oznnni.kkodlebap.presentation.screen.SplashScreen

@Composable
fun MyAppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Playground.route) {
            PlaygroundScreen()
        }
    }
}
