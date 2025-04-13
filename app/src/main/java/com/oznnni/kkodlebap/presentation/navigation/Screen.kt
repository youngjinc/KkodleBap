package com.oznnni.kkodlebap.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash: Screen(route = "splash")
    data object Playground: Screen(route = "playground")
}
