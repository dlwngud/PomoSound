package com.wngud.pomosound.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object Setting : Screen("setting")

    data object Sound : Screen(
        route = "sound/{placeId}",
        navArguments = listOf(navArgument("placeId") {
            type = NavType.IntType
        })
    ) {
        fun createRoute(placeId: Int) = "sound/${placeId}"
    }

    data object Timer : Screen("timer")
}