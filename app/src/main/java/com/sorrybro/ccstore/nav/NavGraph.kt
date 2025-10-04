package com.sorrybro.ccstore.nav

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.auth.AuthManager
import com.sorrybro.ccstore.screen.CardListScreen
import com.sorrybro.ccstore.view.CardViewModel
import java.util.concurrent.Executors

@Composable
fun NavGraph(navController: NavHostController, viewModel: CardViewModel, padding: PaddingValues) {
    val activity = LocalActivity.current
    val executor = remember { Executors.newSingleThreadExecutor() }

    NavHost(navController = navController, startDestination = NavRoutes.LIST) {
        composable(NavRoutes.LIST) {
            val canAuthenticate = activity != null && AuthManager.canAuthenticate(activity)
            var isAuthenticated by rememberSaveable { mutableStateOf(canAuthenticate) }
            var isAuthenticating by rememberSaveable { mutableStateOf(false) }

            if (isAuthenticated) {
                CardListScreen(viewModel = viewModel, padding = padding)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.85f else 1f,
                            label = "scaleAnimation"
                        )

                        IconButton(
                            onClick = {
                                if (!isAuthenticating && activity != null) {
                                    isAuthenticating = true
                                    AuthManager.authenticate(
                                        activity = activity,
                                        executor = executor,
                                        onSuccess = {
                                            isAuthenticated = true
                                            isAuthenticating = false
                                        },
                                        onError = { _, _ ->
                                            isAuthenticating = false
                                        }
                                    )
                                }
                            },
                            interactionSource = interactionSource,
                            modifier = Modifier.scale(scale)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = stringResource(R.string.authenticate),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        Text(
                            text = if (isAuthenticating) stringResource(R.string.authenticating)
                            else stringResource(R.string.tap_to_authenticate)
                        )
                    }
                }
            }
        }
    }
}
