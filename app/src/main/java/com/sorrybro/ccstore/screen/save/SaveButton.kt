package com.sorrybro.ccstore.screen.save

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardEntity
import com.sorrybro.ccstore.data.CardNetwork
import com.sorrybro.ccstore.view.CardViewModel

@Composable
fun SaveButton(
    name: String,
    cardNumber: String,
    expiry: String,
    cvv: String,
    cardNetwork: String,
    bankName: String,
    viewModel: CardViewModel,
    onSaved: () -> Unit,
    initialCard: CardEntity? = null
) {
    val context = LocalContext.current

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
            val isAmex = cardNetwork == CardNetwork.AMERICAN_EXPRESS.displayName
            val isCvvValid = if (isAmex) cvv.length == 4 else cvv.length == 3
            val requiredLength = if (isAmex) 15 else 16
            val cvvLength = if (isAmex) 4 else 3

            when {
                !isNameValid -> {
                    Toast.makeText(context, context.getString(R.string.toast_name_valid), Toast.LENGTH_SHORT).show()
                }

                !isNumberValid -> {
                    Toast.makeText(context, context.getString(R.string.toast_number_valid, requiredLength), Toast.LENGTH_SHORT).show()
                }

                !isExpiryValid -> {
                    Toast.makeText(context, context.getString(R.string.toast_expiry_valid), Toast.LENGTH_SHORT).show()
                }

                !isCvvValid -> {
                    Toast.makeText(context, context.getString(R.string.toast_cvv_valid, cvvLength), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    if (initialCard == null) {
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
                    } else {
                        viewModel.update(
                            CardEntity(
                                id = initialCard.id,
                                name = name,
                                number = numberRaw,
                                expiry = expiry,
                                cvv = cvv,
                                network = cardNetwork,
                                bankName = bankName
                            )
                        )
                    }
                    onSaved()
                    Toast.makeText(context, context.getString(R.string.card_saved), Toast.LENGTH_SHORT).show()
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = stringResource(R.string.save_card))
    }
}
