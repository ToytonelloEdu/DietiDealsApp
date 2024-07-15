package com.example.dietideals.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dietideals.R
import com.example.dietideals.ui.AppView


@Composable
private fun colorForSelected(selected: Boolean): Color {
    return if(isSystemInDarkTheme())
    {
        colorDarkTheme(selected)
    }
    else {
        colorLightTheme(selected)
    }
}


fun colorDarkTheme(selected: Boolean): Color = colorLightTheme(!selected)

private fun colorLightTheme(selected: Boolean) = if (selected) {
    Color.Black
} else {
    Color.White
}

@Composable
fun AuctionButton(currentView: AppView, modifier: Modifier = Modifier) {
    val isSelected = (currentView == AppView.Auctions || currentView == AppView.Bids)
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent
        )
        ) {
        AuctionIcon(modifier, isSelected)
    }
}

@Composable
private fun AuctionIcon(modifier: Modifier, isSelected: Boolean) {
    Icon(
        painter = painterResource(id = R.drawable.auction_ic),
        contentDescription = "Auction",
        modifier = modifier.size(34.dp),
        tint = colorForSelected(isSelected)
    )
}

@Composable
fun HomeButton(currentView: AppView, modifier: Modifier = Modifier) {
    val isSelected = (currentView == AppView.Home)
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent
        )
    )  {
        HomeIcon(modifier, isSelected)
    }
}

@Composable
private fun HomeIcon(modifier: Modifier, isSelected: Boolean) {
    Icon(
        painter = painterResource(id = R.drawable.home_ic),
        contentDescription = "Home",
        modifier = modifier.size(34.dp),
        tint = colorForSelected(isSelected)
    )
}

@Composable
fun UserButton(currentView: AppView, modifier: Modifier = Modifier) {
    val isSelected = (currentView == AppView.Profile)
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent
        )
    )  {

            Icon(
                painter = painterResource(id = R.drawable.user_ic),
                contentDescription = "User",
                modifier = modifier.size(34.dp),
                tint = colorForSelected(isSelected)
            )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        AuctionButton(currentView = AppView.Home)
        HomeButton(currentView = AppView.Home)
        UserButton(currentView = AppView.Home)
    }

}