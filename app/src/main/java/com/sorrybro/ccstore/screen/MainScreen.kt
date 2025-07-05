package com.sorrybro.ccstore.screen

import android.annotation.SuppressLint
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.nav.NavGraph
import com.sorrybro.ccstore.nav.NavRoutes
import com.sorrybro.ccstore.view.CardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: CardViewModel = viewModel()) {
    val navController = rememberNavController()
    var selected by remember { mutableStateOf("save") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (selected) {
                            "save" -> stringResource(R.string.add_card)
                            "list" -> stringResource(R.string.your_cards)
                            else -> ""
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == NavRoutes.SAVE,
                    onClick = {
                        selected = NavRoutes.SAVE
                        navController.navigate(NavRoutes.SAVE)
                    },
                    label = { Text(stringResource(R.string.save_card)) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selected == NavRoutes.LIST,
                    onClick = {
                        selected = NavRoutes.LIST
                        navController.navigate(NavRoutes.LIST)
                    },
                    label = { Text(stringResource(R.string.saved_cards)) },
                    icon = {}
                )
            }
        }
    ) { padding ->
        NavGraph(navController = navController, viewModel = viewModel, padding = padding)
    }
}
