package com.wngud.pomosound

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wngud.pomosound.navigation.Screen
import com.wngud.pomosound.ui.presentation.home.HomeScreen
import com.wngud.pomosound.ui.presentation.setting.SettingScreen

@Composable
fun PomoSoundApp() {
    val navController = rememberNavController()
    PomoSoundNavHost(
        navController = navController
    )
}

@Composable
fun PomoSoundNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNextClick = { navController.navigate(Screen.Setting.route) },
                onSettingClick = { navController.navigate(Screen.Setting.route) }
            )
        }
        composable(route = Screen.Setting.route) {
            SettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}