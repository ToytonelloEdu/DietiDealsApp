package com.example.dietideals.ui.views

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
    showAll: Boolean,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onAuctioneerClicked: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (fetchState) {
        is HomeFetchState.HomeSuccess -> SuccessHomeView(fetchState.auctions, fetchState.isRefreshing, onAuctionClicked , onAuctioneerClicked, onRefresh, modifier, showAll)
        is HomeFetchState.Error -> SuccessHomeView(fetchState.auctions, fetchState.isRefreshing, onAuctionClicked, onAuctioneerClicked, onRefresh, modifier)
        else -> LoadingView(modifier.fillMaxSize())
    }

}

@Composable
private fun SuccessHomeView(
    auctions: List<Auction>,
    isRefreshing: Boolean,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onAuctioneerClicked: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showAll: Boolean = false
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
                    if (showAll || !auctions[index].hasBeenOverFor(3))
                        HomeAuctionCard(auctions[index], onAuctionClicked, onAuctioneerClicked)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
        HomeView(
            HomeFetchState.HomeSuccess(auctions = emptyList()),
            false,
            { _, _ -> },
            {},
            {})
}