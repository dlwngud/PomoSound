package com.wngud.pomosound

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wngud.pomosound.navigation.Screen
import com.wngud.pomosound.ui.presentation.favoriteSound.FavoriteSoundsScreen
import com.wngud.pomosound.ui.presentation.home.HomeScreen
import com.wngud.pomosound.ui.presentation.setting.SettingScreen
import com.wngud.pomosound.ui.presentation.sound.SoundScreen
import com.wngud.pomosound.ui.presentation.timer.TimerScreen
import com.wngud.pomosound.utils.findActivity

@Composable
fun PomoSoundApp() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PomoSoundNavHost(
            navController = navController
        )
    }
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
                onNextClick = {
                    navController.navigate(Screen.Sound.createRoute(it))
                },
                onSettingClick = { navController.navigate(Screen.Setting.route) }
            )
        }
        composable(
            route = Screen.Setting.route,
            arguments = Screen.Setting.navArguments
        ) {
            SettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Sound.route,
            arguments = Screen.Sound.navArguments
        ) {
            SoundScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(Screen.Timer.createRoute(it)) },
                onFavoriteClick = { navController.navigate(Screen.Favorite.route) }
            )
        }
        composable(
            route = Screen.Timer.route,
            arguments = Screen.Timer.navArguments
        ) {
            TimerScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Screen.Favorite.route) {
            FavoriteSoundsScreen()
        }
    }
}