package com.sorrybro.ccstore.screen.save

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.view.CardViewModel
import kotlinx.coroutines.launch


@Composable
fun SaveButton(
    name: String,
    cardNumber: String,
    expiry: String,
    cvv: String,
    cardNetwork: String,
    bankName: String,
    viewModel: CardViewModel,
    snackbarHostState: SnackbarHostState,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                    onSaved()
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
