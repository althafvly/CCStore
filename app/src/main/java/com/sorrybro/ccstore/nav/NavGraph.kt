package com.sorrybro.ccstore.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sorrybro.ccstore.screen.CardListScreen
import com.sorrybro.ccstore.screen.SaveCardScreen
import com.sorrybro.ccstore.view.CardViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: CardViewModel, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = NavRoutes.SAVE) {
        composable(NavRoutes.SAVE) {
            SaveCardScreen(viewModel = viewModel, padding = padding)
        }
        composable(NavRoutes.LIST) {
            CardListScreen(viewModel = viewModel, padding = padding)
        }
    }
}
