package com.sorrybro.ccstore.screen

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.nav.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(selectedRoute: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                when (selectedRoute) {
                    NavRoutes.SAVE -> stringResource(R.string.add_card)
                    NavRoutes.LIST -> stringResource(R.string.your_cards)
                    else -> ""
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
