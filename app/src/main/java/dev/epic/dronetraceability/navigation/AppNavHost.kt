package dev.epic.dronetraceability.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.toLong
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.epic.dronetraceability.ui.commands.DroneCommandScreen
import dev.epic.dronetraceability.ui.dashboard.DashboardScreen
import dev.epic.dronetraceability.ui.dronedetail.DroneDetailScreen
import dev.epic.dronetraceability.ui.history.DroneHistoryScreen
import dev.epic.dronetraceability.ui.map.DroneMapScreen
import dev.epic.dronetraceability.ui.pov.DronePovScreen
@androidx.media3.common.util.UnstableApi
@kotlin.time.ExperimentalTime
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable(
            "droneDetail/{droneId}",
            arguments = listOf(navArgument("droneId") { type = NavType.StringType })
        ) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getString("droneId")!!
            DroneDetailScreen(navController, droneId)
        }

        composable(
            route = "droneMap/{droneId}",
            arguments = listOf(navArgument("droneId") { type = NavType.StringType })
        ) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getString("droneId") ?: ""
            DroneMapScreen(navController, droneId)
        }


        composable(
            route = "droneHistory/{droneId}",
            arguments = listOf(navArgument("droneId") { type = NavType.StringType })
        ) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getString("droneId") ?: ""
            DroneHistoryScreen(navController, droneId)
        }

        composable(
            route = "dronePov/{model}",
            arguments = listOf(navArgument("model") { type = NavType.StringType })
        ) { backStackEntry ->
            val model = backStackEntry.arguments?.getString("model") ?: ""
            DronePovScreen(navController, model)
        }

        composable(
            route = "droneCommand/{droneId}",
            arguments = listOf(navArgument("droneId") { type = NavType.StringType })
        ) { backStackEntry ->
            val droneId = backStackEntry.arguments?.getString("droneId") ?: ""
            DroneCommandScreen(navController, droneId)
        }


    }
}