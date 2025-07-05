package com.sorrybro.ccstore.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.sorrybro.ccstore.ui.CardNumberTextField
import com.sorrybro.ccstore.view.CardViewModel
import kotlinx.coroutines.launch

@Composable
fun SaveCardScreen(viewModel: CardViewModel, padding: PaddingValues) {
    var name by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardNumberField by remember { mutableStateOf(TextFieldValue("")) }
    var expiryField by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(padding)
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets // prevent double-padding
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.cardholder_name)) }
                )

                CardNumberTextField(
                    cardNumberState = cardNumberField,
                    onCardNumberChange = { cardNumberField = it }
                )

                OutlinedTextField(
                    value = expiryField,
                    onValueChange = { newValue ->
                        val digits = newValue.text.filter { it.isDigit() }.take(4)
                        val formatted = when {
                            digits.length <= 2 -> digits
                            else -> digits.substring(0, 2) + "/" + digits.substring(2)
                        }

                        // Calculate new cursor position
                        val unformattedCursor =
                            newValue.text.take(newValue.selection.start).count { it.isDigit() }
                        val cursorOffset = if (unformattedCursor > 2) 1 else 0
                        val newCursor =
                            (unformattedCursor + cursorOffset).coerceAtMost(formatted.length)

                        expiryField = TextFieldValue(
                            text = formatted,
                            selection = TextRange(newCursor)
                        )
                    },
                    label = { Text(stringResource(R.string.expiry_mm_yy)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = { input ->
                        if (input.length <= 4 && input.all { it.isDigit() }) cvv = input
                    },
                    label = { Text(stringResource(R.string.cvv)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    val number = cardNumberField.text.replace(" ", "")
                    val isNameValid = name.matches(Regex("^[a-zA-Z0-9 ]+$"))
                    val isNumberValid = number.length == 16
                    val expiry = expiryField.text
                    val isExpiryValid = expiry.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))
                    val isCvvValid = cvv.length in 3..4

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
                                    cvv = cvv
                                )
                            )
                            name = ""
                            expiryField = TextFieldValue("")
                            cvv = ""
                            cardNumberField = TextFieldValue("")
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.card_saved))
                            }
                        }
                    }
                }) {
                    Text(context.getString(R.string.save_card))
                }
            }
        }
    }
}
