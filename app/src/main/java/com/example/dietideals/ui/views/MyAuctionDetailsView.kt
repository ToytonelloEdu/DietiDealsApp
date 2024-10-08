package com.example.dietideals.ui.views

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.components.AcceptButton
import com.example.dietideals.ui.components.AmountIconText
import com.example.dietideals.ui.components.BuyerIconText
import com.example.dietideals.ui.components.CalendarIconText
import com.example.dietideals.ui.components.DateIconText
import com.example.dietideals.ui.components.ExpandButton
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.NetworkErrorView
import com.example.dietideals.ui.components.TimerIconText

@Composable
fun MyAuctionDetailsView(
    currentState: AuctionFetchState,
    onAccept: (Auction, Bid) -> Unit,
    modifier: Modifier = Modifier,
    onAuctioneerClick: (String) -> Unit,
) {
    when (currentState) {
        is AuctionFetchState.Loading -> LoadingView(modifier.fillMaxSize())
        is AuctionFetchState.Error -> NetworkErrorView(modifier.fillMaxSize())
        is AuctionFetchState.AuctionSuccess -> {
            val auction = currentState.auction
            val primaryColor = auction.medianColor ?: MaterialTheme.colorScheme.primary
            SuccessAuctionDetails(currentState, primaryColor, {}, modifier) { auct ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Offers:",
                            color = primaryColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        if (auct is SilentAuction) {
                            CalendarIconText(
                                auction = auct,
                                primaryColor = primaryColor,
                                underlineLength = 150.dp,
                                fontSize = 16.sp
                            )
                        }
                    }
                    MyAuctionInteractionCard(auction = auct, primaryColor = primaryColor, onAccept = onAccept, onBidderClick = onAuctioneerClick)
                }
            }
        }
    }
}

@Composable
fun MyAuctionInteractionCard(
    auction: Auction,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    onBidderClick: (String) -> Unit,
    onAccept: (Auction, Bid) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(1.dp, primaryColor, RoundedCornerShape(3.dp))
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if(auction.bids.isEmpty()) {
            Text(text = "No bids")
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val bids = auction.bids
                items(bids.size) {
                    val reverseIndex = (bids.size -1) - it
                    when (auction) {
                        is IncrementalAuction -> IncrementalBidPill(auction, bids[reverseIndex], it, primaryColor = primaryColor, onBidderClick= onBidderClick)
                        is SilentAuction -> {
                            val winner = (bids[reverseIndex].bidder ?: bids[reverseIndex].buyer?.username) == auction.acceptedBid?.bidder
                            SilentBidPill(
                                bid = bids[reverseIndex],
                                onAccept = { bid -> onAccept(auction, bid) },
                                primaryColor = primaryColor,
                                onBidderClick = onBidderClick,
                                enabled = !auction.isAuctionOver(),
                                winner = winner
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SilentBidPill(
    bid: Bid,
    onAccept: (Bid) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    onBidderClick: (String) -> Unit,
    winner: Boolean = false,
) {
    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = if(winner) Color(0xFFDFBC0A) else primaryColor,
                shape = RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp)
            )
            .clip(RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp))
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AmountIconText(bid.amount, primaryColor)
            BuyerIconText(bid.bidder ?: bid.buyer?.username ?: "Unknown", primaryColor, onBidderClick)
            ExpandButton(isExpanded, { isExpanded = !isExpanded }, primaryColor)
        }
        if (isExpanded) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateIconText(bid.timeToDate(), primaryColor, underlineLength = 135.dp)
                AcceptButton({onAccept(bid)}, enabled, primaryColor)
            }
        }
    }
}

@Composable
fun IncrementalBidPill(
    auction: IncrementalAuction,
    bid: Bid,
    index: Int,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    onBidderClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
            .padding(8.dp)
            .border(1.dp, primaryColor, RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp))
            .clip(RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp))
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AmountIconText(bid.amount, primaryColor)
            Spacer(modifier = Modifier.width(12.dp))
            BuyerIconText(bid.bidder ?: bid.buyer?.username ?: "Unknown", primaryColor, onBidderClick)
        }
        if (index == 0) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimerIconText(
                    auction = auction,
                    primaryColor = primaryColor
                )
            }
        }
    }
}