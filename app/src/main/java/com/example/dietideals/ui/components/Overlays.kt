package com.example.dietideals.ui.components

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
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
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.Notification
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.theme.DietiDealsTheme
import com.example.dietideals.ui.views.AddTagPill
import com.example.dietideals.ui.views.TagPill
import com.example.dietideals.ui.views.TagsIconLabel
import java.sql.Timestamp

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
fun EditWebsiteDialog(
    correctUrl: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        var website by rememberSaveable { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)

        ) {
            Text(text = "Insert your website url:")
            TextField(
                value = website, onValueChange = {website = it},
                isError = !correctUrl,
                supportingText = { if(!correctUrl) Text(text = "This is not a URL")}
            )
            ConfirmButton(onConfirm = { onConfirm(website) })
        }
    }
}

@Composable
fun AddSocialsDialog(
    absentSocials: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        var showMenu by rememberSaveable { mutableStateOf(false) }
        var selectedSocial by rememberSaveable { mutableStateOf<String?>(null) }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            showMenu = true
                        }
                ){
                    Text(
                        text = selectedSocial ?: "Select a Social",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Icon(
                        imageVector = if (showMenu) Icons.Default.KeyboardArrowUp
                        else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    absentSocials.forEach {
                        Log.i("Overlays", it)
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            onClick = { selectedSocial = it; showMenu = false }
                        )
                    }
                }
            }
            Text(text = "Insert your socials url:")
            var socialUrl by rememberSaveable { mutableStateOf("") }
            TextField(
                value = socialUrl, onValueChange = {socialUrl = it},
                //isError = !correctUrl,
                //supportingText = { if(!correctUrl) Text(text = "This is not a URL")}
            )
            ConfirmButton(
                enabled = selectedSocial != null,
                onConfirm = { onConfirm(selectedSocial!!, socialUrl) }
            )
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
            SearchTagPillsRow(tags = searchQuery.tags.value, screenFraction = screenFraction) {
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
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit,
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
                .fillMaxHeight(screenFraction / 1.25f)
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
                            NotificationCard(
                                notification = notifications[it],
                                onNotificationClick = onNotificationClick
                            )
                        }
                    }
                } else {
                    Text("No Notifications")
                }
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
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
private fun SearchTagPillsRow(
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
            notifications = listOf(
                Notification(
                    1,
                    IncrementalAuction(
                        1,
                        emptyList(),
                        Color.Cyan,
                        "test",
                        "test",
                        null,
                        "Toytonello",
                        Timestamp(System.currentTimeMillis()),
                        mutableListOf(),
                        null,
                        listOf(Tag("test")),
                        3600,
                        5.0,
                        1.0
                    ),
                    Auctioneer(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        Timestamp(System.currentTimeMillis()),
                        null,
                        mutableListOf()
                    ),
                    Timestamp(System.currentTimeMillis()),
                    "OWNER",
                    read = false,
                    received = true
                ),
                Notification(
                    1,
                    IncrementalAuction(
                        1,
                        emptyList(),
                        Color.Cyan,
                        "test",
                        "test",
                        null,
                        "Toytonello",
                        Timestamp(System.currentTimeMillis()),
                        mutableListOf(),
                        null,
                        listOf(Tag("test")),
                        3600,
                        5.0,
                        1.0
                    ),
                    Auctioneer(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        Timestamp(System.currentTimeMillis()),
                        null,
                        mutableListOf()
                    ),
                    Timestamp(System.currentTimeMillis()),
                    "OWNER",
                    read = true,
                    received = true
                ),
            ),
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