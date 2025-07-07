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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.sorrybro.ccstore.data.BankName
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.view.CardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveCardScreen(viewModel: CardViewModel, padding: PaddingValues) {
    var name by rememberSaveable { mutableStateOf("") }
    var bankName by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var cardNetwork by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    val supportedNetworks = remember { CardNetwork.displayNames() }
    val supportedBanks = remember { BankName.displayNames() }
    var networkExpanded by remember { mutableStateOf(false) }
    var bankNameExpanded by remember { mutableStateOf(false) }

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
                            name = input.filter { it.isLetter() || it.isWhitespace() }
                        },
                        label = { Text(stringResource(R.string.cardholder_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = bankNameExpanded,
                        onExpandedChange = { bankNameExpanded = !bankNameExpanded }
                    ) {
                        TextField(
                            value = bankName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.bank_name)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = bankNameExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = bankNameExpanded,
                            onDismissRequest = { bankNameExpanded = false }
                        ) {
                            supportedBanks.forEach { name ->
                                DropdownMenuItem(
                                    text = { Text(name.ifEmpty { stringResource(R.string.none) }) },
                                    onClick = {
                                        bankName = name
                                        bankNameExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = networkExpanded,
                        onExpandedChange = { networkExpanded = !networkExpanded }
                    ) {
                        TextField(
                            value = cardNetwork,
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
                                        cardNetwork = network
                                        networkExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    var internalCardNumber by remember {
                        mutableStateOf(TextFieldValue(cardNumber))
                    }

                    OutlinedTextField(
                        enabled = cardNetwork.isNotEmpty(),
                        value = internalCardNumber,
                        onValueChange = { newValue ->
                            val digits = newValue.text.filter { it.isDigit() }
                            val maxLength =
                                if (cardNetwork == CardNetwork.AMERICAN_EXPRESS.displayName) 15 else 16
                            val limitedDigits = digits.take(maxLength)
                            val formatted = limitedDigits.chunked(4).joinToString(" ")

                            // Count digits before old cursor
                            val digitsBeforeCursor = newValue.text
                                .take(newValue.selection.start)
                                .count { it.isDigit() }

                            // Compute new cursor position
                            var digitCount = 0
                            var newCursor = 0
                            for ((i, c) in formatted.withIndex()) {
                                if (c.isDigit()) digitCount++
                                if (digitCount == digitsBeforeCursor + 1) {
                                    newCursor = i + 1
                                    break
                                } else if (digitCount == digitsBeforeCursor) {
                                    newCursor = i + 1
                                }
                            }

                            // Prevent cursor from jumping beyond input
                            val finalCursor =
                                if (formatted.length == internalCardNumber.text.length &&
                                    formatted == internalCardNumber.text
                                ) {
                                    internalCardNumber.selection.start
                                } else {
                                    newCursor.coerceAtMost(formatted.length)
                                }

                            internalCardNumber = TextFieldValue(
                                text = formatted,
                                selection = TextRange(finalCursor)
                            )

                            cardNumber = formatted
                        },
                        label = { Text(stringResource(R.string.card_number)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    var internalExpiry by remember {
                        mutableStateOf(TextFieldValue(expiry))
                    }

                    OutlinedTextField(
                        enabled = cardNetwork.isNotEmpty(),
                        value = internalExpiry,
                        onValueChange = { newValue ->
                            val digits = newValue.text.filter { it.isDigit() }.take(4)

                            val formatted = when {
                                digits.length <= 2 -> digits
                                else -> digits.substring(0, 2) + "/" + digits.substring(2)
                            }

                            // Count digits before cursor
                            val digitsBeforeCursor = newValue.text
                                .take(newValue.selection.start)
                                .count { it.isDigit() }

                            // Calculate new cursor position
                            var digitCount = 0
                            var newCursor = 0
                            for ((i, c) in formatted.withIndex()) {
                                if (c.isDigit()) digitCount++
                                if (digitCount == digitsBeforeCursor + 1) {
                                    newCursor = i + 1
                                    break
                                } else if (digitCount == digitsBeforeCursor) {
                                    newCursor = i + 1
                                }
                            }

                            // Avoid cursor jump if content unchanged
                            val finalCursor = if (formatted == internalExpiry.text) {
                                internalExpiry.selection.start
                            } else {
                                newCursor.coerceAtMost(formatted.length)
                            }

                            internalExpiry = TextFieldValue(
                                text = formatted,
                                selection = TextRange(finalCursor)
                            )

                            expiry = formatted
                        },
                        label = { Text(stringResource(R.string.expiry_mm_yy)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        enabled = cardNetwork.isNotEmpty(),
                        value = cvv,
                        onValueChange = { input ->
                            val length =
                                if (cardNetwork == CardNetwork.AMERICAN_EXPRESS.displayName) 4 else 3
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
                            val numberRaw = cardNumber.replace(" ", "")
                            val isNameValid = name.matches(Regex("^[a-zA-Z ]+$"))
                            val isNumberValid = when (cardNetwork) {
                                CardNetwork.AMERICAN_EXPRESS.displayName -> numberRaw.length == 15
                                CardNetwork.VISA.displayName,
                                CardNetwork.MASTERCARD.displayName,
                                CardNetwork.RUPAY.displayName,
                                CardNetwork.DISCOVER.displayName -> numberRaw.length == 16

                                else -> numberRaw.length in 13..19
                            }
                            val isExpiryValid = expiry.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))
                            val isCvvValid =
                                if (cardNetwork == CardNetwork.AMERICAN_EXPRESS.displayName) cvv.length == 4 else cvv.length == 3

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
                                            number = numberRaw,
                                            expiry = expiry,
                                            cvv = cvv,
                                            network = cardNetwork,
                                            bankName = bankName
                                        )
                                    )
                                    name = ""
                                    expiry = ""
                                    cvv = ""
                                    cardNumber = ""
                                    cardNetwork = ""
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
