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

@Composable
fun ExpiryField(
    expiry: String,
    cardNetwork: String,
    onExpiryChange: (String) -> Unit
) {
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

            val finalCursor = if (formatted == internalExpiry.text) {
                internalExpiry.selection.start
            } else {
                newCursor.coerceAtMost(formatted.length)
            }

            internalExpiry = TextFieldValue(
                text = formatted,
                selection = TextRange(finalCursor)
            )

            onExpiryChange(formatted)
        },
        label = { Text(stringResource(R.string.expiry_mm_yy)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
