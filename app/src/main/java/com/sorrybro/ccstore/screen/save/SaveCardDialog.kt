package com.sorrybro.ccstore.screen.save

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.view.CardViewModel

@Composable
fun SaveCardDialog(
    viewModel: CardViewModel,
    onDismissRequest: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        shape = RoundedCornerShape(16.dp),
        text = {
            SaveCardForm(
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                onDismissRequest = onDismissRequest
            )
        }
    )

    // Optional: can add a SnackbarHost outside of AlertDialog if needed
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp),
        snackbar = {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(12.dp),
                snackbarData = it
            )
        }
    )
}
