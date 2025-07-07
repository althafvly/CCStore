package com.sorrybro.ccstore.screen.save

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardNetwork


@Composable
fun CardNumberField(
    cardNumber: String,
    cardNetwork: String,
    onNumberChange: (String) -> Unit
) {
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

            // Count digits before cursor
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

            // Prevent cursor from jumping unnecessarily
            val finalCursor = if (formatted == internalCardNumber.text) {
                internalCardNumber.selection.start
            } else {
                newCursor.coerceAtMost(formatted.length)
            }

            internalCardNumber = TextFieldValue(
                text = formatted,
                selection = TextRange(finalCursor)
            )

            onNumberChange(formatted)
        },
        label = { Text(stringResource(R.string.card_number)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp)
    )
}
