package com.sorrybro.ccstore.nav

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.auth.AuthManager
import com.sorrybro.ccstore.screen.CardListScreen
import com.sorrybro.ccstore.screen.SaveCardScreen
import com.sorrybro.ccstore.view.CardViewModel
import java.util.concurrent.Executors

@Composable
fun NavGraph(navController: NavHostController, viewModel: CardViewModel, padding: PaddingValues) {
    val activity = LocalActivity.current
    val executor = remember { Executors.newSingleThreadExecutor() }

    NavHost(navController = navController, startDestination = NavRoutes.SAVE) {
        composable(NavRoutes.SAVE) {
            SaveCardScreen(viewModel = viewModel, padding = padding)
        }
        composable(NavRoutes.LIST) {
            var authTriggered by rememberSaveable { mutableStateOf(false) }
            var isAuthenticated by rememberSaveable { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                if (!authTriggered && activity != null) {
                    authTriggered = true
                    AuthManager.authenticate(
                        activity = activity,
                        executor = executor,
                        onSuccess = {
                            isAuthenticated = true
                        },
                        onError = { _, _ ->
                            navController.navigate(NavRoutes.SAVE)
                        }
                    )
                }
            }

            if (isAuthenticated) {
                CardListScreen(viewModel = viewModel, padding = padding)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.authenticating))
                }
            }
        }
    }
}
