package com.sorrybro.ccstore.screen

import android.annotation.SuppressLint
import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.BankName
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.screen.save.SaveCardDialog
import com.sorrybro.ccstore.screen.view.CardItem
import com.sorrybro.ccstore.screen.view.DeleteCardDialog
import com.sorrybro.ccstore.screen.view.FilterDropdown
import com.sorrybro.ccstore.view.CardViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CardListScreen(viewModel: CardViewModel, padding: PaddingValues) {
    val cards by viewModel.cards.collectAsState()
    val supportedNetworks = remember { CardNetwork.displayNames() }
    val supportedBanks = remember { BankName.displayNames().filter { it.isNotBlank() } }

    // Cache configuration to avoid recomposition lag
    val configuration = LocalConfiguration.current
    val screenWidth by remember(configuration) { mutableStateOf(configuration.screenWidthDp.dp) }
    val boxWidth by remember(screenWidth) { mutableStateOf(screenWidth * 0.4f) } // 40% width

    var selectedBank by remember { mutableStateOf<String?>(null) }
    var selectedNetwork by remember { mutableStateOf<String?>(null) }

    // Filtered and sorted cards are memoized to avoid recomposition lag
    val filteredCards by remember(cards, selectedBank, selectedNetwork) {
        derivedStateOf {
            cards.asSequence()
                .filter { card ->
                    (selectedBank == null || card.bankName == selectedBank) &&
                            (selectedNetwork == null || card.network == selectedNetwork)
                }
                .sortedBy { it.bankName.lowercase() }
                .toList()
        }
    }

    val clipboardManager = LocalClipboard.current
    var cardToDelete by remember { mutableStateOf<CardEntity?>(null) }
    var cardToEdit by remember { mutableStateOf<CardEntity?>(null) }

    // Delete dialog
    if (cardToDelete != null) {
        DeleteCardDialog(
            onConfirm = {
                viewModel.delete(cardToDelete!!)
                cardToDelete = null
            },
            onDismiss = { cardToDelete = null }
        )
    }

    // Edit dialog
    if (cardToEdit != null) {
        SaveCardDialog(
            viewModel = viewModel,
            onDismissRequest = { cardToEdit = null },
            initialCard = cardToEdit
        )
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Filter row at the top
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            FilterDropdown(
                label = stringResource(R.string.bank),
                options = supportedBanks,
                selectedOption = selectedBank,
                onOptionSelected = { selectedBank = it },
                boxWidth = boxWidth
            )

            FilterDropdown(
                label = stringResource(R.string.network),
                options = supportedNetworks,
                selectedOption = selectedNetwork,
                onOptionSelected = { selectedNetwork = it },
                boxWidth = boxWidth
            )
        }

        // Card list items
        filteredCards.forEach { card ->
            CardItem(
                card = card,
                onCopy = { label, value ->
                    val clip = ClipData.newPlainText(label, value)
                    clipboardManager.nativeClipboard.setPrimaryClip(clip)
                },
                onDelete = { cardToDelete = it },
                onEdit = { cardToEdit = it }
            )
        }
    }
}