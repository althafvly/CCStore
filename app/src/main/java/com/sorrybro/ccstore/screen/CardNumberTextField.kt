package com.sorrybro.ccstore.screen

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.sorrybro.ccstore.R

@Composable
fun CardNumberTextField(
    cardNumberState: TextFieldValue,
    onCardNumberChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = cardNumberState,
        onValueChange = { newValue ->
            val digits = newValue.text.filter { it.isDigit() }.take(16)

            // Format with spaces after every 4 digits
            val formatted = digits.chunked(4).joinToString(" ")

            // Compute new cursor offset
            val newCursorPos = newValue.selection.start

            // Count how many spaces we added before the cursor
            var spaceCount = 0
            for (i in digits.indices) {
                if (i > 0 && i % 4 == 0 && i < newCursorPos + spaceCount) {
                    spaceCount++
                }
            }

            val finalCursor = (newValue.selection.start + spaceCount).coerceAtMost(formatted.length)

            onCardNumberChange(
                TextFieldValue(
                    text = formatted,
                    selection = TextRange(finalCursor)
                )
            )
        },
        label = { Text(stringResource(R.string.card_number)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
