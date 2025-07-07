package com.sorrybro.ccstore.screen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sorrybro.ccstore.R
import com.sorrybro.ccstore.data.CardEntity

@Composable
fun CardItem(
    card: CardEntity,
    onCopy: (label: String, value: String) -> Unit,
    onDelete: (CardEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF374ABE), Color(0xFF64B6FF))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            IconButton(
                onClick = { onDelete(card) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Column {
                    CardRow(label = card.bankName)
                    Spacer(modifier = Modifier.height(8.dp))

                    CardRowWithCopy(
                        label = card.number,
                        onCopy = { onCopy("number", card.number) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CardRowWithCopy(
                        label = stringResource(R.string.expires) + " " + card.expiry,
                        onCopy = { onCopy("expiry", card.expiry) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CardRowWithCopy(
                        label = stringResource(R.string.cvv) + ": " + card.cvv,
                        onCopy = { onCopy("cvv", card.cvv) }
                    )
                }
            }

            Text(
                text = card.network,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 2.dp)
            )
        }
    }
}
