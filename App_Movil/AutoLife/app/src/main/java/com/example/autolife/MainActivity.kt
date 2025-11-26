package com.example.autolife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.autolife.data.AppDatabase
import com.example.autolife.ui.* import com.example.autolife.viewmodel.AuthViewModel
import com.example.autolife.viewmodel.AuthViewModelFactory
import com.example.autolife.viewmodel.CarViewModel
import com.example.autolife.viewmodel.CarViewModelFactory

// Rutas de Navegación
object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CAR_LIST = "carList"
    const val REGISTER_CAR = "registerCar"
    const val CAR_DETAILS = "carDetails/{carId}"
    const val REGISTER_MAINTENANCE = "registerMaintenance/{carId}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)

    // ⬅️ INYECCIÓN DE VIEWMODELS USANDO LAS FACTORIES CORREGIDAS
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(database.userDao()))
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory())

    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) Destinations.CAR_LIST else Destinations.LOGIN
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Destinations.CAR_LIST) { popUpTo(Destinations.LOGIN) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Destinations.REGISTER) }
            )
        }

        composable(Destinations.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate(Destinations.CAR_LIST) { popUpTo(Destinations.LOGIN) { inclusive = true } } },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Destinations.CAR_LIST) {
            CarListScreen(
                viewModel = carViewModel,
                authViewModel = authViewModel,
                onAddCarClick = { navController.navigate(Destinations.REGISTER_CAR) },
                onCarSelected = { carId -> navController.navigate("carDetails/$carId") }
            )
        }

        composable(Destinations.REGISTER_CAR) {
            RegisterCarScreen(
                viewModel = carViewModel,
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Destinations.CAR_DETAILS) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: 0
            CarDetailScreen(
                carId = carId,
                viewModel = carViewModel,
                onNavigateBack = { navController.popBackStack() },
                onAddMaintenanceClick = { id -> navController.navigate("registerMaintenance/$id") }
            )
        }

        composable(Destinations.REGISTER_MAINTENANCE) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: 0
            RegisterMaintenanceScreen(
                carId = carId,
                viewModel = carViewModel,
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}