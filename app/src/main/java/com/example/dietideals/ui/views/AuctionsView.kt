package com.example.dietideals.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.MyAuctionCard
import com.example.dietideals.ui.components.SwipeRefresh

@Composable
fun AuctionsView(
    vendorUserState: UserState.Vendor,
    onAuctionClicked: (Auction) -> Unit,
    onAuctioneerClicked: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auctioneer = vendorUserState.auctioneer
    SwipeRefresh(isRefreshing = isRefreshing, onRefresh = { onRefresh() }) {
        if (auctioneer.auctions.isEmpty()) {
            Column(
                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No auctions created")
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val auctions = auctioneer.auctions
                items(auctions.size) { index ->
                    MyAuctionCard(auctions[index], onAuctionClicked, onAuctioneerClicked)
                }
            }
        }
    }
}