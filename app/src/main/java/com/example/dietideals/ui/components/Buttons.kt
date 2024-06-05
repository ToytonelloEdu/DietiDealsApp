package com.example.dietideals.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dietideals.R
import com.example.dietideals.ui.AppView

@Composable
fun AuctionButton(currentView: AppView, modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
        ) {
        if (currentView == AppView.Auctions || currentView == AppView.Bids) {
            Image(
                painter = painterResource(id = R.drawable.auction_ic),
                contentDescription = "Auction",
                contentScale = ContentScale.Fit,
                modifier = modifier.size(34.dp)
            )
        }
        else {
            Icon(
                painter = painterResource(id = R.drawable.auction_ic),
                contentDescription = "User",
                modifier = modifier.size(34.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun HomeButton(currentView: AppView, modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    )  {
        if (currentView == AppView.Home) {
            Image(
                painter = painterResource(id = R.drawable.home_ic),
                contentDescription = "Home",
                contentScale = ContentScale.Fit,
                modifier = modifier.size(34.dp)
            )
        }
        else {
            Icon(
                painter = painterResource(id = R.drawable.home_ic),
                contentDescription = "User",
                modifier = modifier.size(34.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun UserButton(currentView: AppView, modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    )  {
        if (currentView == AppView.Profile) {
            Image(
                painter = painterResource(id = R.drawable.user_ic),
                contentDescription = "User",
                contentScale = ContentScale.Fit,
                modifier = modifier.size(34.dp)
            )
        }
        else {
            Icon(
                painter = painterResource(id = R.drawable.user_ic),
                contentDescription = "User",
                modifier = modifier.size(34.dp),
                tint = Color.White
            )
        }
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