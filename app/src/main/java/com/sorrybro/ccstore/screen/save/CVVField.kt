package com.sorrybro.ccstore.screen.save

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardNetwork


@Composable
fun CVVField(
    cvv: String,
    cardNetwork: String,
    onCvvChange: (String) -> Unit
) {
    val maxLength = if (cardNetwork == CardNetwork.AMERICAN_EXPRESS.displayName) 4 else 3

    OutlinedTextField(
        enabled = cardNetwork.isNotEmpty(),
        value = cvv,
        onValueChange = { input ->
            if (input.length <= maxLength && input.all { it.isDigit() }) {
                onCvvChange(input)
            }
        },
        label = { Text(stringResource(R.string.cvv)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}