package com.sorrybro.ccstore.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sorrybro.ccstore.nav.NavGraph
import com.sorrybro.ccstore.nav.NavRoutes
import com.sorrybro.ccstore.view.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CardViewModel = viewModel()) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val selected = currentDestination ?: NavRoutes.SAVE

    Scaffold(
        topBar = {
            MainTopBar(selectedRoute = selected)
        },
        bottomBar = {
            MainBottomBar(
                selectedRoute = selected,
                onNavigate = { route ->
                    if (route != selected) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavGraph(navController = navController, viewModel = viewModel, padding = padding)
    }
}
