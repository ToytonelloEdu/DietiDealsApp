package com.example.dietideals.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dietideals.R
import com.example.dietideals.data.network.serializables.NetAuction
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.AuctionFetchState
import com.example.dietideals.ui.components.AuctioneerIconText
import com.example.dietideals.ui.components.BidIconButton
import com.example.dietideals.ui.components.CalendarIconText
import com.example.dietideals.ui.components.CancelButton
import com.example.dietideals.ui.components.ConfirmButton
import com.example.dietideals.ui.components.DateIconText
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.MoneyIcon
import com.example.dietideals.ui.components.NetworkErrorView
import com.example.dietideals.ui.components.PreviousIcon
import com.example.dietideals.ui.components.PriceIconText
import com.example.dietideals.ui.components.SubmitButton
import com.example.dietideals.ui.components.TagIcon
import com.example.dietideals.ui.components.TimePlusIconText
import com.example.dietideals.ui.components.TimerIconText
import com.example.dietideals.ui.components.UnderLine
import com.example.dietideals.ui.components.toFormattedDateTime
import com.example.dietideals.ui.theme.primaryLight
import kotlinx.coroutines.delay

@Composable
fun AuctionDetailsView(
    currentState: AuctionFetchState,
    directBid: Boolean,
    onSubmit: (Auction, Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (currentState) {
        is AuctionFetchState.Loading -> LoadingView(modifier.fillMaxSize())
        is AuctionFetchState.Error -> NetworkErrorView(modifier.fillMaxSize())
        is AuctionFetchState.AuctionSuccess -> {
            val auction = currentState.auction
            val primaryColor = auction.medianColor ?: MaterialTheme.colorScheme.primary
            SuccessAuctionDetails(currentState, primaryColor) { auct ->
                Column (Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {

                    AuctionInteractionCard(
                        primaryColor = primaryColor,
                    ) {
                        when (auct) {
                            is IncrementalAuction -> AuctionInteraction(auct, primaryColor, directBid, onSubmit)
                            is SilentAuction -> AuctionInteraction(auct, primaryColor, onSubmit)
                        }
                    }

                    if(auct is IncrementalAuction) IncrementalBidsModalSheet(primaryColor, auct)
                }
            }
        }
    }
}

@Composable
fun SuccessAuctionDetails(
    successState: AuctionFetchState.AuctionSuccess,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    centerContent: @Composable (Auction) -> Unit
) {
    val auction = successState.auction;
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AuctionInfoColumn(auction, primaryColor, auction.pictures)
        centerContent(auction)
        AuctionTagsGrid(auction.tags, primaryColor)

    }
}

@Composable
fun AuctionInfoColumn(auction: Auction, primaryColor: Color, photos: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PhotosLazyRow(primaryColor, photos)
        AuctioneerIconText(
            auction,
            primaryColor,
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            170.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = auction.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 15.sp,
            lineHeight = 16.sp
        )
        UnderLine(primaryColor = primaryColor, distance = 8.dp, thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
fun PhotosLazyRow(primaryColor: Color, photos: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.CenterStart
    ) {
        if (photos.isNotEmpty()) {
            LazyRow {
                items(photos.size) { index ->
                    val restUrl = stringResource(R.string.restapi_url)
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data("${restUrl}photos/${photos[index]}").build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .border(1.dp, primaryColor, RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                    )
                }
            }
        } else {
            Row (
                modifier = Modifier
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = "No photos found",
                        modifier = Modifier
                            .size(120.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AuctionInteractionCard(
    primaryColor: Color,
    modifier: Modifier = Modifier,
    cardContent: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .border(1.dp, primaryColor, MaterialTheme.shapes.medium),
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            cardContent()
        }

    }
}

@Composable
fun AuctionInteraction(
    auction: IncrementalAuction,
    primaryColor: Color,
    directBid: Boolean = false,
    onSubmit: (Auction, Double) -> Unit
) {
    Text(
        "Offers",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        color = primaryColor,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
    )
    BidsInfoFields(auction, primaryColor)
    BidInteraction(auction, primaryColor, directBid, onSubmit)
}

@Composable
fun BidsInfoFields(auction: IncrementalAuction, primaryColor: Color) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StartBidRow(auction, primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        LastBidRow(auction, primaryColor)
    }
}

@Composable
private fun StartBidRow(auction: IncrementalAuction, primaryColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Start:", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateIconText(auction.dateToDate(), primaryColor, underlineLength = 136.dp)
            PriceIconText(amount = auction.startingPrice, primaryColor = primaryColor)
        }
    }
}



@Composable
private fun LastBidRow(auction: IncrementalAuction, primaryColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Last:", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val lastBid = auction.getLastBidOrBidsLast()
            if (lastBid != null ) {
                DateIconText(lastBid.timeToDate(), primaryColor, underlineLength = 136.dp)
                PriceIconText(amount = lastBid.amount, primaryColor = primaryColor)
            } else {
                Text(
                    text = "No bids",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }

        }
    }
}

@Composable
fun BidInteraction(auction: IncrementalAuction, primaryColor: Color, directBid: Boolean = false, onSubmit: (Auction, Double) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var stateDirectBid by rememberSaveable { mutableStateOf(directBid) }
        var showDialog by rememberSaveable { mutableStateOf(false) }
        TimerIconText(auction, primaryColor, underlineDistance = 4.dp, updating = true)
        BidIconButton(auction, primaryColor, {_,_ -> showDialog = true }, timeInterval = auction.timeInterval)

        LaunchedEffect(stateDirectBid) {
            if(stateDirectBid) delay(800)

            showDialog = stateDirectBid
        }

        if(showDialog) {
            IncrementalConfirmDialog({ showDialog = false; stateDirectBid = false }, primaryColor, auction, onSubmit)
        }
    }
}

@Composable
private fun IncrementalConfirmDialog(
    onDismiss: () -> Unit,
    primaryColor: Color,
    auction: IncrementalAuction,
    onSubmit: (Auction, Double) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(200.dp, 200.dp)
                .background(Color.White)
                .border(1.dp, primaryColor),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text("Bid Summary", modifier = Modifier.padding(start = 8.dp))
            PriceIconText(
                amount = auction.calculateNextAmount(),
                primaryColor = primaryColor,
                modifier = Modifier.padding(start = 8.dp)
            )
            TimePlusIconText(
                timeInterval = auction.timeInterval,
                primaryColor = primaryColor,
                textColor = Color.Unspecified,
                fontWeight = null,
                underLine = true,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 8.dp)
            ) {
                Text("Confirm")
                ConfirmButton(
                    onConfirm = { onSubmit(auction, auction.calculateNextAmount()) },
                    text = "Yes",
                    modifier = Modifier.width(40.dp),
                    width = Dp.Unspecified
                )
                CancelButton(
                    onCancel = { onDismiss() },
                    text = "No",
                    modifier = Modifier.width(40.dp),
                    width = Dp.Unspecified
                )
            }
        }
    }
}

@Composable
fun AuctionInteraction(
    auction: SilentAuction,
    primaryColor: Color,
    onSubmit: (Auction, Double) -> Unit,
) {
    var offerValue by rememberSaveable { mutableStateOf("") }
    Text(
        "Make secret offer:",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        color = primaryColor,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
    )
    TextField(
        value = offerValue,
        label = { Text("Offer") },
        onValueChange = { offerValue = it },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(50.dp),
        leadingIcon = {MoneyIcon(primaryColor, Modifier.padding(horizontal = 8.dp))}
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ExpLabelIconText(auction, primaryColor)
        SubmitButton(auction, primaryColor, offerValue, onSubmit = onSubmit)
    }
}

@Composable
fun ExpLabelIconText(auction: SilentAuction, primaryColor: Color) {
    Row {
        Text(
            text = "Exp:",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        CalendarIconText(auction, primaryColor, underlineLength = 136.dp)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun IncrementalBidsModalSheet(
    primaryColor: Color,
    auction: IncrementalAuction,
) {
    var showBids by rememberSaveable { mutableStateOf(false) }
    ShowBidsButton(showBids, { showBids = !showBids }, Modifier)
    if (showBids) {
        ModalBottomSheet(
            onDismissRequest = { showBids = false },
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = "Offers history:",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = primaryColor,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                items(auction.bids.size) {
                    val reverseIndex = (auction.bids.size - 1) - it
                    IncrementalBidPill(
                        auction,
                        auction.bids[reverseIndex],
                        it,
                        primaryColor = primaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun ShowBidsButton(showBids: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviousIcon(primaryColor = MaterialTheme.colorScheme.tertiary)
        Text(
            text = "Show previous offers",
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic
        )
    }
}


@Composable
fun AuctionTagsGrid(tags: List<Tag>, primaryColor: Color, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TagsIconLabel(
            primaryColor,
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        TagsLazyStaggeredGrid(tags, primaryColor, Modifier)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TagsIconLabel(
    primaryColor: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    iconSize: Dp = 16.dp,
    textAlign: TextAlign = TextAlign.Center,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TagIcon(primaryColor, Modifier.size(iconSize))
        Text(
            text = "Tags:",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = textAlign
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsLazyStaggeredGrid(tags: List<Tag>, primaryColor: Color, modifier: Modifier = Modifier) {
    if(tags.isNotEmpty()) {
        FlowRow (
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
        ) {
            tags.forEach { tag ->
                TagPill(tag, primaryColor)
            }
        }
    } else {
        Text(text = "No tags", color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
fun TagPill(tag: Tag, primaryColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .height(30.dp)
            .background(primaryColor)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = tag.tagName,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp),
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
fun InteractionCardPreview() {
    val auction = SilentAuction(
        netAuction = NetAuction(
            null,
            "Incremental Auction",
            emptyList(),
            null,
            "Prova",
            "Prova",
            null,
            null,
            "2024-07-20 12:00:00",
            expirationDate = "2024-07-30 12:00:00"
        )
    )
    AuctionInteractionCard(
        primaryLight
    ) { AuctionInteraction(auction, primaryLight) { _, _ -> } }
}

