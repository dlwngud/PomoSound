package com.wngud.pomosound.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object Setting : Screen("setting")

    data object Sound : Screen("sound")

    data object Timer : Screen("timer")
}