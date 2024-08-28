package com.example.dietideals.ui.components

import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.theme.DietiDealsTheme
import java.sql.Timestamp

@Composable
fun SilentConfirmDialog(
    onDismiss: () -> Unit,
    auction: SilentAuction,
    onSubmit: (SilentAuction) -> Unit,
    primaryColor: Color,
    money: Double
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(180.dp, 180.dp)
                .background(Color.White)
                .border(1.dp, primaryColor),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text("Secret Bid Summary", modifier = Modifier.padding(start = 8.dp))
            PriceIconText(
                amount = money,
                primaryColor = primaryColor,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.9f).padding(start = 8.dp)
            ) {
                Text("Confirm")
                ConfirmButton(
                    onConfirm = { onSubmit(auction) },
                    text = "Yes",
                    modifier = Modifier.width(40.dp),
                    width = Dp.Unspecified
                )
                CancelButton(
                    onCancel = { onDismiss() },
                    text = "No",
                    modifier = Modifier.width(40.dp),
                    width = Dp.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSilentConfirmDialog() {
    DietiDealsTheme {
        SilentConfirmDialog(
            auction = SilentAuction(
                id = 1,
                objectName = "test",
                description = "test",
                date = Timestamp(System.currentTimeMillis()),
                tags = listOf(Tag("test")),
                bids = mutableListOf(),
                expirationDate = Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
            ),
            onDismiss = {},
            onSubmit = {_, ->},
            primaryColor = MaterialTheme.colorScheme.primary,
            money = 25.0
        )
    }
}