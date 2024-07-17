package com.example.dietideals.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dietideals.ui.models.Auction

@Composable
fun AuctionCard(auction: Auction, modifier: Modifier = Modifier) {
    Card (
        modifier
    ) {
        Text(auction.toString())
    }
}