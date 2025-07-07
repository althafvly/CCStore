package com.sorrybro.ccstore.screen

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.screen.view.CardItem
import com.sorrybro.ccstore.screen.view.DeleteCardDialog
import com.sorrybro.ccstore.view.CardViewModel

@Composable
fun CardListScreen(viewModel: CardViewModel, padding: PaddingValues) {
    val cards by viewModel.cards.collectAsState()
    val sortedCards by remember(cards) {
        derivedStateOf { cards.sortedBy { it.bankName.lowercase() } }
    }
    val clipboardManager = LocalClipboard.current
    var cardToDelete by remember { mutableStateOf<CardEntity?>(null) }

    if (cardToDelete != null) {
        DeleteCardDialog(
            onConfirm = {
                viewModel.delete(cardToDelete!!)
                cardToDelete = null
            },
            onDismiss = { cardToDelete = null }
        )
    }

    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sortedCards, key = { it.id }) { card ->
                CardItem(
                    card = card,
                    onCopy = { label, value ->
                        val clip = ClipData.newPlainText(label, value)
                        clipboardManager.nativeClipboard.setPrimaryClip(clip)
                    },
                    onDelete = { cardToDelete = it }
                )
            }
        }
    }
}
