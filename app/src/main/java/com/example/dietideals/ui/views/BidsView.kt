package com.example.dietideals.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.MyBidAuctionCard
import com.example.dietideals.ui.components.SwipeRefresh

@Composable
fun BidsView(
    bidderUserState: UserState.Bidder,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val buyer = bidderUserState.buyer
    SwipeRefresh(isRefreshing = false, onRefresh = { /*TODO*/ }) {
        if (buyer.bids.isEmpty()) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No bids placed")
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bids = buyer.bids
                items(bids.size) { index ->
                    MyBidAuctionCard(bids[index].auction!!, onAuctionClicked)
                }
            }
        }
    }

}