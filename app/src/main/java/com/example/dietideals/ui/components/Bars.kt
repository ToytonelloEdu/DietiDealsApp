package com.example.dietideals.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dietideals.R
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.AppView
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.ProfileFetchState
import com.example.dietideals.ui.theme.topAppBarColors

val upNavigableScreens = listOf(
    AppView.AuctionDetails,
    AppView.SignUp,
    AppView.MyAuctionDetails,
    AppView.MyBidAuctionDetails,
    AppView.NewAuction
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: AppView,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    textForTopBar: @Composable () -> String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        title = { Text(text = textForTopBar()) },
        colors = topAppBarColors(),
        navigationIcon = {
            if (canNavigateBack && currentScreen in upNavigableScreens) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        modifier = modifier.size(35.dp),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            actions()
        }
    )
}

@Composable
fun textForTopBar(
    currentScreen: AppView,
    auctionState: AuctionFetchState,
    userState: UserState,
    otherUserState: ProfileFetchState
) : String {
    return when (currentScreen) {
        AppView.AuctionDetails,
        AppView.MyAuctionDetails,
        AppView.MyBidAuctionDetails,
        -> {
            return when (auctionState) {
                is AuctionFetchState.AuctionSuccess -> auctionState.auction.objectName
                else -> stringResource(id = currentScreen.title)
            }

        }
        AppView.Profile -> {
            when (userState) {
                is UserState.Vendor -> "@${userState.auctioneer.username}"
                is UserState.Bidder -> "@${userState.buyer.username}"
                else -> stringResource(id = currentScreen.title)
            }
        }
        AppView.UserDetails -> {
            when (otherUserState) {
                is ProfileFetchState.ProfileSuccess -> "@${otherUserState.user.username}"
                else -> stringResource(id = currentScreen.title)
            }
        }
        else -> stringResource(id = currentScreen.title)
    }

}

@Composable
fun AppBottomBar(
    currentScreen: AppView,
    userState: UserState,
    onAuctionClick: () -> Unit,
    onHomeClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationEnabled: Boolean = true
) {
    BottomAppBar (
        modifier = modifier
            .height(66.dp)
            .let {
                if (!isSystemInDarkTheme()) it.border(0.5.dp, Color.LightGray)
                else it.border(0.25.dp, Color.Black)
            }
            ,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AuctionButton(currentScreen, userState, enabled = navigationEnabled) { onAuctionClick() }
                if (userState !is UserState.Vendor) HomeButton(currentScreen, enabled = navigationEnabled) { onHomeClick() }
                UserButton(currentScreen, enabled = navigationEnabled) { onUserClick() }
            }
    }
}

@Composable
fun AuctionDetailsTopBarIcon(auctionState: AuctionFetchState) {
    when (auctionState) {
        is AuctionFetchState.AuctionSuccess -> {
            val auction = auctionState.auction
            Icon(
                painter = iconByAuctionType(auction = auction),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
        }
        is AuctionFetchState.Error -> {}
        else -> throw IllegalArgumentException("Unsupported auction state")
    }

}

@Preview
@Composable
fun AppBottomBarPreview() {
    AppBottomBar(AppView.Home, UserState.NotLoggedIn(), {}, {}, {})
}