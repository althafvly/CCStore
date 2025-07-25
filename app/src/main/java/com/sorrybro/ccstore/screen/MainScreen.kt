package com.sorrybro.ccstore.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sorrybro.ccstore.nav.NavGraph
import com.sorrybro.ccstore.nav.NavRoutes
import com.sorrybro.ccstore.screen.save.SaveCardDialog
import com.sorrybro.ccstore.view.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: CardViewModel = viewModel()) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val selected = currentDestination ?: NavRoutes.LIST
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MainTopBar(selectedRoute = selected)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        }
    ) { padding ->
        if (showDialog) {
            SaveCardDialog(
                viewModel = viewModel,
                onDismissRequest = { showDialog = false }
            )
        }
        NavGraph(navController = navController, viewModel = viewModel, padding = padding)
    }
}
