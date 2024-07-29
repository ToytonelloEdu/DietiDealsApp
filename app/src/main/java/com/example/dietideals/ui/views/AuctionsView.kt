package com.example.dietideals.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.MyAuctionCard

@Composable
fun AuctionsView(
    vendorUserState: UserState.Vendor,
    onAuctionClicked: (Auction) -> Unit,
    modifier: Modifier = Modifier
) {
    val auctioneer = vendorUserState.auctioneer
    if(auctioneer.auctions.isEmpty()) {
        Column (
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No auctions created")
        }
    } else {
        LazyColumn (
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val auctions = auctioneer.auctions
            items(auctions.size) { index ->
                MyAuctionCard(auctions[index], onAuctionClicked)
            }
        }
    }
}