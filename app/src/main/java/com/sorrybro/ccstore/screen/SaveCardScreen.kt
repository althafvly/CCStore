package com.sorrybro.ccstore.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.view.CardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveCardScreen(viewModel: CardViewModel, padding: PaddingValues) {
    var name by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardNumberField by remember { mutableStateOf(TextFieldValue("")) }
    var cardNetworkField by remember { mutableStateOf(TextFieldValue("")) }
    var expiryField by remember { mutableStateOf(TextFieldValue("")) }
    val supportedNetworks = CardNetwork.displayNames()
    var networkExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(padding),
                snackbar = { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        snackbarData = data
                    )
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .heightIn(max = 600.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val innerScrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(innerScrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { input ->
                            // Filter input: only letters and spaces allowed
                            val filtered = input.filter { it.isLetter() || it.isWhitespace() }
                            name = filtered
                        },
                        label = { Text(stringResource(R.string.cardholder_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = networkExpanded,
                        onExpandedChange = { networkExpanded = !networkExpanded }
                    ) {
                        TextField(
                            value = cardNetworkField.text,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.card_network)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = networkExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = networkExpanded,
                            onDismissRequest = { networkExpanded = false }
                        ) {
                            supportedNetworks.forEach { network ->
                                DropdownMenuItem(
                                    text = { Text(network) },
                                    onClick = {
                                        cardNetworkField = TextFieldValue(network)
                                        networkExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    CardNumberTextField(
                        enabled = !cardNetworkField.text.isEmpty(),
                        cardNumberState = cardNumberField,
                        onCardNumberChange = {
                            val number = it.text.replace(" ", "")
                            val length =
                                if (cardNetworkField.text == CardNetwork.AMERICAN_EXPRESS.displayName)
                                    15 else 16
                            if (number.length <= length && number.all { it.isDigit() }) cardNumberField =
                                it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        enabled = !cardNetworkField.text.isEmpty(),
                        value = expiryField,
                        onValueChange = { newValue ->
                            val digits = newValue.text.filter { it.isDigit() }.take(4)

                            val validated = buildString {
                                for ((index, char) in digits.withIndex()) {
                                    when (index) {
                                        0 -> if (char == '0' || char == '1') append(char)
                                        1 -> {
                                            val first = this.getOrNull(0)
                                            if (first == '0' && char in '1'..'9') append(char)
                                            else if (first == '1' && char in '0'..'2') append(char)
                                        }

                                        2, 3 -> append(char)
                                    }
                                }
                            }

                            val formatted = when {
                                validated.length <= 2 -> validated
                                else -> validated.substring(0, 2) + "/" + validated.substring(2)
                            }

                            val unformattedCursor = newValue.text
                                .take(newValue.selection.start)
                                .count { it.isDigit() }

                            val cursorOffset = if (unformattedCursor > 2) 1 else 0
                            val newCursor = (unformattedCursor + cursorOffset)
                                .coerceAtMost(formatted.length)

                            expiryField = TextFieldValue(
                                text = formatted,
                                selection = TextRange(newCursor)
                            )
                        },
                        label = { Text(stringResource(R.string.expiry_mm_yy)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        enabled = !cardNetworkField.text.isEmpty(),
                        value = cvv,
                        onValueChange = { input ->
                            val length =
                                if (cardNetworkField.text == CardNetwork.AMERICAN_EXPRESS.displayName) 4 else 3
                            if (input.length <= length && input.all { it.isDigit() }) cvv = input
                        },
                        label = { Text(stringResource(R.string.cvv)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val number = cardNumberField.text.replace(" ", "")
                            val isNameValid = name.matches(Regex("^[a-zA-Z ]+$"))
                            val isNumberValid = when (cardNetworkField.text) {
                                CardNetwork.AMERICAN_EXPRESS.displayName -> number.length == 15
                                CardNetwork.VISA.displayName,
                                CardNetwork.MASTERCARD.displayName,
                                CardNetwork.RUPAY.displayName,
                                CardNetwork.DISCOVER.displayName -> number.length == 16

                                else -> number.length in 13..19
                            }
                            val expiry = expiryField.text
                            val isExpiryValid = expiry.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))
                            val isCvvValid = when (cardNetworkField.text) {
                                CardNetwork.AMERICAN_EXPRESS.displayName -> cvv.length == 4
                                else -> cvv.length == 3
                            }
                            val network = cardNetworkField.text

                            when {
                                !isNameValid -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.toast_name_valid))
                                }

                                !isNumberValid -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.toast_number_valid))
                                }

                                !isExpiryValid -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.toast_expiry_valid))
                                }

                                !isCvvValid -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.toast_cvv_valid))
                                }

                                else -> {
                                    viewModel.save(
                                        CardEntity(
                                            name = name,
                                            number = number,
                                            expiry = expiry,
                                            cvv = cvv,
                                            network = network
                                        )
                                    )
                                    name = ""
                                    expiryField = TextFieldValue("")
                                    cvv = ""
                                    cardNumberField = TextFieldValue("")
                                    cardNetworkField = TextFieldValue("")
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(context.getString(R.string.card_saved))
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.save_card))
                    }
                }
            }
        }
    }
}
