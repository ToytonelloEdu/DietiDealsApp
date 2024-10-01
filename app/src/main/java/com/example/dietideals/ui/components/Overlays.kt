package com.example.dietideals.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dietideals.domain.auxiliary.SearchQuery
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.theme.DietiDealsTheme
import com.example.dietideals.ui.views.AddTagPill
import com.example.dietideals.ui.views.TagPill
import com.example.dietideals.ui.views.TagsIconLabel

@Composable
fun SilentConfirmDialog(
    onDismiss: () -> Unit,
    auction: SilentAuction,
    onSubmit: (SilentAuction) -> Unit,
    primaryColor: Color,
    money: Double
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(170.dp, 170.dp)
                .background(Color.White)
                .border(1.dp, primaryColor),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text("Secret Bid Summary", modifier = Modifier.padding(start = 8.dp))
            PriceIconText(
                amount = money,
                primaryColor = primaryColor,
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
                    onConfirm = { onSubmit(auction) },
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
fun SearchDialog(
    searchQuery: SearchQuery,
    onValueChange: (SearchQuery) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (SearchQuery) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    screenFraction: Float = 0.95f
) {
    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { onDismiss() }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .drawBehind {
                    drawLine(
                        color = primaryColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(vertical = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures { }
                }

        ) {
            TextField(
                value = searchQuery.objectName.value,
                onValueChange = { searchQuery.objectName.value = it; onValueChange(searchQuery) },
                modifier = Modifier.fillMaxWidth(screenFraction),
                label = { Text("Object") },
                leadingIcon = {
                    GavelIcon(
                        primaryColor = primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            TextField(
                value = searchQuery.vendor.value,
                onValueChange = { searchQuery.vendor.value = it; onValueChange(searchQuery) },
                modifier = Modifier.fillMaxWidth(screenFraction),
                label = { Text("Auctioneer") },
                leadingIcon = { AuctioneerIcon(primaryColor = primaryColor) }
            )
            TagPillsRow(tags = searchQuery.tags.value, screenFraction = screenFraction) {
                onValueChange(searchQuery)
            }
            Row(
                modifier = Modifier.fillMaxWidth(screenFraction),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                SearchTextButton(
                    primaryColor = primaryColor,
                ) { onConfirm(searchQuery) }
            }
        }
    }
}


@Composable
fun NotificationsDialog(
    notifications: List<String>,
    onNotificationClick: (String) -> Unit,
    onValueChange: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    screenFraction: Float = 0.65f
) {
    Box (
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { onDismiss() }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            modifier = modifier
                .padding(top = 16.dp, end = 16.dp)
                .fillMaxWidth(screenFraction)
                .fillMaxHeight(screenFraction/1.25f)
                .background(Color.White)
                .border(1.dp, primaryColor)
                .pointerInput(Unit) {
                    detectTapGestures { }
                }

        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                if (notifications.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(notifications.size) {
                            Text(
                                notifications[it],
                                modifier = Modifier.clickable { onNotificationClick(notifications[it]) }
                            )
                        }
                    }
                } else {
                    Text("No Notifications")
                }
                Row(
                    Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Clear All")
                    Text("Read All")
                }
            }
        }
    }
}


@Composable
private fun TagPillsRow(
    tags: MutableList<String>,
    screenFraction: Float,
    onValueChange: () -> Unit,

    ) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(screenFraction)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TagsIconLabel(
            primaryColor = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            iconSize = 14.dp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(screenFraction),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(tags.size) {
                TagPill(
                    tag = Tag(tags[it]),
                    primaryColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        tags.removeAt(it); onValueChange()
                    }
                )
            }
            if(tags.size < 3)
                item {
                    AddTagPill(primaryColor = MaterialTheme.colorScheme.primary) {
                        showDialog = true
                    }
                }
            item{
                Text("${tags.size}/3")
            }
        }
    }

    if(showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(
                modifier = Modifier.size(200.dp, 200.dp)
            ) {
                var newTag by rememberSaveable { mutableStateOf("") }
                TextField(
                    value = newTag,
                    onValueChange = {newTag = it}
                )
                ConfirmButton(
                    onConfirm = {
                        tags.add(newTag)
                        onValueChange()
                        showDialog = false
                    },
                )
            }
        }
    }
}


@Preview(showBackground = false, showSystemUi = true)
@Composable
fun PreviewNotificationsDialog() {
    DietiDealsTheme {
        NotificationsDialog(
            notifications = listOf("Ciro è scemo"),
            onValueChange = {},
            onDismiss = {},
            onNotificationClick = {}
        )
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun PreviewSearchDialog() {
    DietiDealsTheme {
        SearchDialog(
            searchQuery = SearchQuery(),
            onValueChange = {},
            onDismiss = {},
            onConfirm = {}
        )
    }
}


//SilentConfirmDialog(
//            auction = SilentAuction(
//                id = 1,
//                objectName = "test",
//                description = "test",
//                date = Timestamp(System.currentTimeMillis()),
//                tags = listOf(Tag("test")),
//                bids = mutableListOf(),
//                expirationDate = Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
//            ),
//            onDismiss = {},
//            onSubmit = {_, ->},
//            primaryColor = MaterialTheme.colorScheme.primary,
//            money = 25.0
//        )