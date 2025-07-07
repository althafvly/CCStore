package com.sorrybro.ccstore.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.nav.NavRoutes

@Composable
fun MainBottomBar(
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        NavigationBarItem(
            selected = selectedRoute == NavRoutes.SAVE,
            onClick = { onNavigate(NavRoutes.SAVE) },
            label = { Text(stringResource(R.string.save_card)) },
            icon = { Icon(Icons.Default.Add, contentDescription = null) }
        )

        NavigationBarItem(
            selected = selectedRoute == NavRoutes.LIST,
            onClick = { onNavigate(NavRoutes.LIST) },
            label = { Text(stringResource(R.string.saved_cards)) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
        )
    }
}
