package com.example.dietideals.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dietideals.ui.HomeFetchState
import com.example.dietideals.ui.components.HomeAuctionCard
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.NetworkErrorView
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.ui.components.SwipeRefresh

@Composable
fun HomeView(
    fetchState: HomeFetchState,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (fetchState) {
        is HomeFetchState.HomeSuccess -> SuccessHomeView(fetchState, onAuctionClicked, onRefresh, modifier)
        is HomeFetchState.Error -> NetworkErrorView(modifier.fillMaxSize(), onRetry)
        else -> LoadingView(modifier.fillMaxSize())
    }

}

@Composable
private fun SuccessHomeView(
    successState: HomeFetchState.HomeSuccess,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    SwipeRefresh(
        isRefreshing = successState.isRefreshing,
        onRefresh = onRefresh
    ){
        if (successState.auctions.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                Text(text = "No auctions found", modifier = Modifier)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                val auctions = successState.auctions
                items(auctions.size) { index ->
                    if (!auctions[index].hasBeenOverFor(3))
                        HomeAuctionCard(successState.auctions[index], onAuctionClicked)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
        HomeView(HomeFetchState.HomeSuccess(auctions = emptyList()), { _, _ -> },{}, {})
}