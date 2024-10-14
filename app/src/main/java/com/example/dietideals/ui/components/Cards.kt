package com.example.dietideals.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dietideals.R
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.Notification
import com.example.dietideals.domain.models.SilentAuction

@Composable
fun HomeAuctionCard(
    auction: Auction,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onAuctioneerClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = auction.medianColor ?: MaterialTheme.colorScheme.primary
) {
    Card (
        modifier
            .height(300.dp)
            .padding(8.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .border(1.dp, primaryColor, MaterialTheme.shapes.medium)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            AuctionTopBar(auction, primaryColor, onAuctionClicked)

            Box(Modifier.fillMaxSize()) {

                Column (Modifier.padding(16.dp)) {
                    AuctionInfoRow(auction, primaryColor, onAuctioneerClick = onAuctioneerClicked)

                    AuctionInteractionRow(auction, primaryColor, onAuctionClicked, Modifier.padding(top = 16.dp))
                }

            }
        }
    }
}


@Composable
fun MyAuctionCard(
    auction: Auction,
    onAuctionClicked: (Auction) -> Unit,
    onAuctioneerClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = auction.medianColor ?: MaterialTheme.colorScheme.primary
) {
    Card(
        modifier
            .height(225.dp)
            .padding(10.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .border(1.dp, primaryColor, MaterialTheme.shapes.medium)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            AuctionTopBar(auction, primaryColor, {auction,_ -> onAuctionClicked(auction)})
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    MyAuctionInfoRow(auction, primaryColor, onAuctioneerClicked)
                }
            }

        }
    }
}

@Composable
fun MyBidAuctionCard(
    auction: Auction,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    onAuctioneerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = auction.medianColor ?: MaterialTheme.colorScheme.primary
) {
    Card(
        modifier
            .height(240.dp)
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .border(1.dp, primaryColor, MaterialTheme.shapes.medium)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            AuctionTopBar(auction, primaryColor, onAuctionClicked)
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    MyBidAuctionInfoRow(auction, primaryColor, onAuctioneerClick, onAuctionClicked)
                }
            }

        }
    }
}

@Composable
fun MyBidAuctionInfoRow(
    auction: Auction,
    primaryColor: Color,
    onAuctioneerClick: (String) -> Unit,
    onAuctionClicked: (Auction, Boolean) -> Unit
) {
    Row (
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val restUrl = stringResource(R.string.restapi_url)
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("${restUrl}photos/${auction.pictures.firstOrNull() ?: "none"}").build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .aspectRatio(1f)
                .border(1.dp, primaryColor, RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.green_auction_ic),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            when (auction) {
                is IncrementalAuction -> {
                    IncrementalAuctionLastBid(auction, primaryColor, onAuctioneerClick)
                    BidIconButton(auction, primaryColor, onAuctionClicked, Modifier.fillMaxWidth(), auction.timeInterval)
                }

                is SilentAuction -> {
                    SilentAuctionLastBid(auction, primaryColor, onAuctioneerClick)
                    BidIconButton(auction, primaryColor, onAuctionClicked, Modifier.fillMaxWidth())
                }
            }

        }
    }
}


@Composable
fun MyAuctionInfoRow(auction: Auction, primaryColor: Color, onAuctioneerClick: (String) -> Unit) {
    Row (
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val restUrl = stringResource(R.string.restapi_url)
        val picture = auction.pictures.firstOrNull() ?: "none"
        Log.d("MyAuctionInfoRow", "Picture: $picture")
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("${restUrl}photos/${picture}").build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .border(1.dp, primaryColor, RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.green_auction_ic),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Last Bid:",
                style = TextStyle(
                    color = primaryColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            when (auction) {
                is IncrementalAuction -> { IncrementalAuctionLastBid(auction, primaryColor, onAuctioneerClick)}
                is SilentAuction -> { SilentAuctionLastBid(auction, primaryColor, onAuctioneerClick) }
            }
        }
    }
}

@Composable
fun IncrementalAuctionLastBid(auction: IncrementalAuction, primaryColor: Color, onClick: (String) -> Unit) {
    val lastBid = auction.getLastBidOrBidsLast()
    if(lastBid != null){
        BidderIconText(lastBid, primaryColor, Modifier.fillMaxWidth(), 210.dp, FontWeight.Medium, onClick)
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriceIconText(lastBid.amount, primaryColor)
            TimerIconText(auction, primaryColor, underlineDistance = 2.dp)
        }
    } else {
        Text(
            text = "No bids yet",
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun SilentAuctionLastBid(auction: SilentAuction, primaryColor: Color, onAuctioneerClick: (String) -> Unit) {
    val lastBid = auction.bids.lastOrNull()
    if(lastBid != null){
        BidderIconText(
            lastBid = lastBid,
            primaryColor = primaryColor,
            modifier = Modifier.fillMaxWidth(),
            underlineLength = 210.dp,
            fontWeight = FontWeight.Medium,
            onClick = onAuctioneerClick
        )
    } else {
        Text(
            text = "No bids yet",
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
        )
    }
    CalendarIconText(auction = auction, primaryColor = primaryColor, Modifier.fillMaxWidth(), 210.dp, 16.sp)
}


@Composable
private fun AuctionTopBar(
    auction: Auction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier
            .fillMaxWidth()
            .background(color = primaryColor)
            .height(50.dp)
            .clickable { onAuctionClicked(auction, false) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(25.dp),
                painter = iconByAuctionType(auction),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                auction.objectName,
                modifier = Modifier,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )
        }
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(35.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go to Auction Page",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun AuctionInfoRow(
    auction: Auction,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    onAuctioneerClick: (String) -> Unit
) {
    Row (
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val restUrl = stringResource(R.string.restapi_url)
        val picture = auction.pictures.firstOrNull() ?: "none"
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("${restUrl}photos/${picture}").build(),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .border(1.dp, primaryColor, RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.green_auction_ic),
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            AuctioneerIconText(auction, primaryColor, onClick = onAuctioneerClick)
            AuctionDescriptionText(auction)
        }
    }
}

@Composable
private fun AuctionDescriptionText(auction: Auction) {
    Text(
        text = auction.description,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(start = 4.dp)
            .size(160.dp, 80.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = 13.sp,
        lineHeight = 14.sp
    )
}

@Composable
private fun AuctionInteractionRow(
    auction: Auction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (auction) {
            is IncrementalAuction -> IncrAuctionInteraction(auction, primaryColor, onAuctionClicked)
            is SilentAuction -> SilentAuctionInteraction(auction, primaryColor, onAuctionClicked)
            else -> throw IllegalArgumentException("Unknown auction type")
        }
    }


}

@Composable
private fun IncrAuctionInteraction(
    auction: IncrementalAuction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit
) {
        if (auction.lastBid == null) {
            Text(
                text = "No bids yet",
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Light
            )
            BidIconButton(auction, primaryColor, onAuctionClicked)
        } else {
            PriceIconText(auction.lastBid.amount, primaryColor)
            TimerIconText(auction, primaryColor, underlineDistance = 4.dp)
            BidIconButton(auction, primaryColor, onAuctionClicked)
        }
}

@Composable
private fun SilentAuctionInteraction(
    auction: SilentAuction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit,
) {
    ExpirationIconText(auction, primaryColor)
    BidIconButton(auction, primaryColor, onAuctionClicked)
}

@Composable
fun NotificationCard(
    notification: Notification,
    onNotificationClick: (Notification) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary
) {
    val read = notification.read
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = 60.dp)
            .background(
                color = if (read) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(bottomStart = 5.dp ,bottomEnd = 5.dp)
            )
            .drawRoundedUnderline(primaryColor)
            .clip(RoundedCornerShape(bottomStart = 5.dp ,bottomEnd = 5.dp))
            .clickable { onNotificationClick(notification) }
    ) {
        Text(
            text = notification.toString(),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if(read) Color.Gray else Color.Black

            ),
            modifier = Modifier
                .fillMaxSize(0.9f)
                .padding(all = 8.dp)
        )
    }
}

fun Modifier.drawRoundedUnderline(
    color: Color,
    radius: Dp = 5.dp
): Modifier {
    return this.drawBehind {
        val cornerRadiusPx = radius.toPx()

        // Create a rounded rectangle path
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    bottomLeftCornerRadius = CornerRadius(cornerRadiusPx),
                    bottomRightCornerRadius = CornerRadius(cornerRadiusPx)
                ),
            )
        }

        clipPath(path) {

            // Draw Bottom Border
            drawLine(
                color = color,
                start = Offset(0.0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

