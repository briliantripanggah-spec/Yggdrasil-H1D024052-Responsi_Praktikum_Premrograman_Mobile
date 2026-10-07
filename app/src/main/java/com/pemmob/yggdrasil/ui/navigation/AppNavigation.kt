package com.pemmob.yggdrasil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.yggdrasil.ui.screen.DetailScreen
import com.pemmob.yggdrasil.ui.screen.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{malId}"
    fun detail(malId: Int) = "detail/$malId"
}

private fun NavBackStackEntry.isResumed(): Boolean =
    lifecycle.currentState == Lifecycle.State.RESUMED

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { entry ->
            HomeScreen(
                onAnimeClick = { malId ->
                    if (entry.isResumed()) {
                        navController.navigate(Routes.detail(malId))
                    }
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("malId") { type = NavType.IntType })
        ) { entry ->
            DetailScreen(
                onBack = {
                    if (entry.isResumed()) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
