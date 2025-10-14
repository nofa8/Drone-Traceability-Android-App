package dev.epic.dronetraceability.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.toLong
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.epic.dronetraceability.ui.dashboard.DashboardScreen
import dev.epic.dronetraceability.ui.dronedetail.DroneDetailScreen
import dev.epic.dronetraceability.ui.map.DroneMapScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable("droneDetail/{droneId}", arguments = listOf(navArgument("droneId") {type = NavType.LongType})) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getLong("droneId") ?: 0L
            DroneDetailScreen(navController, droneId)
        }
        composable(
            route = "droneMap/{droneId}",
            arguments = listOf(navArgument("droneId") { type = NavType.LongType })
        ) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getLong("droneId") ?: 0
            DroneMapScreen(navController, droneId)
        }


    }
}