package com.wngud.pomosound

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wngud.pomosound.navigation.Screen
import com.wngud.pomosound.ui.presentation.home.HomeScreen
import com.wngud.pomosound.ui.presentation.setting.SettingScreen
import com.wngud.pomosound.ui.presentation.sound.SoundScreen
import com.wngud.pomosound.ui.presentation.timer.TimerScreen
import com.wngud.pomosound.utils.findActivity

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
    val context = LocalContext.current
    val activity = context.findActivity()

    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            Screen.Timer.route -> {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            else -> {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNextClick = { navController.navigate(Screen.Sound.route) },
                onSettingClick = { navController.navigate(Screen.Setting.route) }
            )
        }
        composable(route = Screen.Setting.route) {
            SettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Sound.route) {
            SoundScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(Screen.Timer.route) }
            )
        }
        composable(route = Screen.Timer.route) {
            TimerScreen()
        }
    }
}