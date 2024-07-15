package com.example.dietideals.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dietideals.ui.FetchState
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.NetworkErrorView

@Composable
fun HomeView(
    fetchState: FetchState,
    modifier: Modifier = Modifier
) {
    when (fetchState) {
        is FetchState.Loading -> LoadingView(modifier.fillMaxSize())
        is FetchState.HomeSuccess -> SuccessHomeView(fetchState, modifier)
        is FetchState.Error -> NetworkErrorView(modifier.fillMaxSize())
    }

}

@Composable
private fun SuccessHomeView(successState: FetchState.HomeSuccess, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = successState.auctions.toString()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
        HomeView(FetchState.HomeSuccess(auctions = "emptyList"))
}