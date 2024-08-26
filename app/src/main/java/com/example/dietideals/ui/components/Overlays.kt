package com.example.dietideals.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.theme.DietiDealsTheme
import java.sql.Timestamp

@Composable
fun SilentConfirmDialog(
    auction: SilentAuction,
) {
    Text("Prova")
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
            )
        )
    }
}