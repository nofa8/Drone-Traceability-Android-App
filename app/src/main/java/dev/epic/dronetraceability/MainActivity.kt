package dev.epic.dronetraceability

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.epic.dronetraceability.navigation.AppNavHost

import dev.epic.dronetraceability.ui.dashboard.DashboardScreen
import dev.epic.dronetraceability.ui.dronedetail.DroneDetailScreen
import dev.epic.dronetraceability.ui.map.DroneMapScreen
import dev.epic.dronetraceability.ui.theme.DroneTraceabilityTheme

@kotlin.time.ExperimentalTime
class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroneTraceabilityTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}


sealed class DroneScreen(val route: String) {
    object Dashboard : DroneScreen("dashboard")
    object Detail : DroneScreen("droneDetail/{droneId}") {
        fun createRoute(droneId: String) = "droneDetail/$droneId"
    }
    object Map : DroneScreen("droneMap/{droneId}") {
        fun createRoute(droneId: String) = "droneMap/$droneId"
    }

    object History : DroneScreen("droneHistory/{droneId}") {
        fun createRoute(droneId: String) = "droneHistory/$droneId"
    }

    object Pov : DroneScreen("dronePov/{model}") {
        fun createRoute(model: String) = "dronePov/$model"
    }

    object Commands : DroneScreen("droneCommand/{droneId}") {
        fun createRoute(droneId: String) = "droneCommand/$droneId"
    }

}