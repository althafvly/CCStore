package com.sorrybro.ccstore.screen.save

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.data.BankName
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.view.CardViewModel


@Composable
fun SaveCardForm(
    viewModel: CardViewModel,
    snackbarHostState: SnackbarHostState
) {
    var name by rememberSaveable { mutableStateOf("") }
    var bankName by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var cardNetwork by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }

    val supportedNetworks = remember { CardNetwork.displayNames() }
    val supportedBanks = remember { BankName.displayNames() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(max = 600.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardNameField(name) { name = it }

            BankDropdown(bankName, supportedBanks) { bankName = it }

            NetworkDropdown(cardNetwork, supportedNetworks) { cardNetwork = it }

            CardNumberField(cardNumber, cardNetwork) {
                cardNumber = it
            }

            ExpiryField(expiry, cardNetwork) {
                expiry = it
            }

            CVVField(cvv, cardNetwork) { cvv = it }

            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(
                name, cardNumber, expiry, cvv, cardNetwork, bankName,
                viewModel, snackbarHostState
            ) {
                name = ""
                expiry = ""
                cvv = ""
                cardNumber = ""
                cardNetwork = ""
            }
        }
    }
}
