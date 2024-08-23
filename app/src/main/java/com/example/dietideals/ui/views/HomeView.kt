package com.example.dietideals.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dietideals.ui.HomeFetchState
import com.example.dietideals.ui.components.HomeAuctionCard
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.components.SwipeRefresh

@Composable
fun HomeView(
    fetchState: HomeFetchState,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (fetchState) {
        is HomeFetchState.HomeSuccess -> SuccessHomeView(fetchState.auctions, fetchState.isRefreshing, onAuctionClicked, onRefresh, modifier)
        is HomeFetchState.Error -> SuccessHomeView(fetchState.auctions, fetchState.isRefreshing, onAuctionClicked, onRefresh, modifier)
        else -> LoadingView(modifier.fillMaxSize())
    }

}

@Composable
private fun SuccessHomeView(
    auctions: List<Auction>,
    isRefreshing: Boolean,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    SwipeRefresh(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ){
        if (auctions.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "No auctions found", modifier = Modifier)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {

                items(auctions.size) { index ->
                    if (!auctions[index].hasBeenOverFor(3))
                        HomeAuctionCard(auctions[index], onAuctionClicked)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
        HomeView(HomeFetchState.HomeSuccess(auctions = emptyList()), { _, _ -> }, {})
}