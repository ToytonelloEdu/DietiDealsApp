package com.example.dietideals.ui.views

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.dietideals.R
import com.example.dietideals.domain.auxiliary.AuctionType
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.Seconds
import com.example.dietideals.domain.models.Tag
import com.example.dietideals.ui.NewAuctionState
import com.example.dietideals.ui.components.AuctioneerIconText
import com.example.dietideals.ui.components.CalendarIcon
import com.example.dietideals.ui.components.CalendarIconText
import com.example.dietideals.ui.components.CameraIconButton
import com.example.dietideals.ui.components.CancelButton
import com.example.dietideals.ui.components.ConfirmButton
import com.example.dietideals.ui.components.GalleryIconButton
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.MoneyIcon
import com.example.dietideals.ui.components.MoneyPlusIcon
import com.example.dietideals.ui.components.TimerIcon
import com.example.dietideals.ui.components.createImageFile
import com.example.dietideals.ui.components.getCameraLauncher
import com.example.dietideals.ui.components.getGalleryLauncher
import com.example.dietideals.ui.components.toYYYYFormattedDate
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAuctionView(
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
    newAuctionState: NewAuctionState,
    exitView: () -> Unit,
    onConfirmClick: (NewAuction) -> Unit,
    screenFraction: Float = 0.9f
) {
    if(newAuctionState is NewAuctionState.Error) {
        NewAuctionErrorDialog(onValueChange, newAuction, newAuctionState)
    }
    if(newAuctionState is NewAuctionState.Loading) {
        NewAuctionLoadingDialog(onValueChange, newAuction)
    }
    if(newAuctionState is NewAuctionState.Success) {
        NewAuctionSuccessDialog(exitView)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        AddPhotosLazyRow(newAuction, onValueChange)
        StaticFieldsRow(screenFraction, newAuction)

        TextField(
            value = newAuction.objectName,
            onValueChange = { newAuction.objectName = it; onValueChange(newAuction) },
            label = { Text("Object name") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filled_auction_ic),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(screenFraction)
        )

        TextField(
            value = newAuction.description,
            onValueChange = { newAuction.description = it; onValueChange(newAuction) },
            label = { Text("Description") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                )
            },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth(screenFraction)
                .height(80.dp)

        )

        TagPillsRow(screenFraction, newAuction, onValueChange)

        var selectedIndex: Int? by rememberSaveable {
            mutableStateOf(newAuction.auctionType?.index)
        }
        val options = listOf(AuctionType.IncrementalAuction, AuctionType.SilentAuction)
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier
                .fillMaxWidth(screenFraction)
        ) {
            options.forEachIndexed { index, auctionType ->
                SegmentedButton(
                    onClick = { selectedIndex = index; newAuction.auctionType = auctionType; onValueChange(newAuction) },
                    selected = (index == selectedIndex),
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                    colors = SegmentedButtonDefaults.colors().copy(
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    icon = {}
                ) {
                    Text(
                        text = auctionType.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if(selectedIndex == null) {
                Text(
                    text = "Select one to show additional options",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            } else {
                when (selectedIndex) {
                    0 -> { IncrementalSpecificFields(newAuction, onValueChange, screenFraction) }
                    1 -> { SilentSpecificFields(newAuction, onValueChange, screenFraction) }
                    else -> {}
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(screenFraction)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onCancel = exitView)
                    ConfirmButton(onConfirm = { onConfirmClick(newAuction) })
                }
            }
        }


    }
}

@Composable
private fun StaticFieldsRow(
    screenFraction: Float,
    newAuction: NewAuction,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(screenFraction)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuctioneerIconText(
            auctioneer = newAuction.auctioneer!!.username,
            primaryColor = MaterialTheme.colorScheme.primary,
            underlineDistance = 4.dp
        )
        CalendarIconText(
            date = newAuction.date,
            primaryColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NewAuctionSuccessDialog(exitView: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = { exitView() },
    ) {
        Text(
            text = "SUCCESS",
            modifier = Modifier
                .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NewAuctionLoadingDialog(
    onValueChange: (NewAuction) -> Unit,
    newAuction: NewAuction,
) {
    BasicAlertDialog(
        onDismissRequest = { onValueChange(newAuction) },
    ) {
        LoadingView(Modifier.fillMaxSize())
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NewAuctionErrorDialog(
    onValueChange: (NewAuction) -> Unit,
    newAuction: NewAuction,
    newAuctionState: NewAuctionState.Error,
) {
    BasicAlertDialog(
        onDismissRequest = { onValueChange(newAuction) },
    ) {
        Column(
            modifier = Modifier
                .size(200.dp, 200.dp)
                .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(text = "ERROR", Modifier.padding(8.dp), color = MaterialTheme.colorScheme.error)
            Text(text = newAuctionState.message ?: "Unknown error", Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun TagPillsRow(
    screenFraction: Float,
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
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
            val tags = newAuction.tags
            items(tags.size) {
                TagPill(
                    tag = tags[it],
                    primaryColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        newAuction.tags.removeAt(it); onValueChange(
                        newAuction
                    )
                    }
                )
            }
            item {
                AddTagPill(primaryColor = MaterialTheme.colorScheme.primary) {
                    showDialog = true
                }
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
                ConfirmButton(onConfirm = {
                    newAuction.tags.add(Tag(newTag))
                    onValueChange(newAuction)
                    showDialog = false
                })
            }
        }
    }
}

@Composable
fun IncrementalSpecificFields(
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
    screenFraction: Float
) {
    var startingPrice by rememberSaveable { mutableStateOf("") }
    var raisingThreshold by rememberSaveable { mutableStateOf("") }
    var timeInterval by rememberSaveable { mutableStateOf("") }
    Row (
        modifier = Modifier
            .fillMaxWidth(screenFraction),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = startingPrice,
            onValueChange = { startingPrice = it; ;newAuction.startingPrice = it.toDoubleOrNull(); onValueChange(newAuction) },
            label = { Text("Starting price", fontSize = 13.sp) },
            leadingIcon = { MoneyIcon(primaryColor = MaterialTheme.colorScheme.primary) },
            singleLine = true,
            modifier = Modifier.weight(0.4f),
            isError = startingPrice != "" && startingPrice.toDoubleOrNull() == null,
            supportingText = {
                if (startingPrice != "" && startingPrice.toDoubleOrNull() == null) {
                    Text(
                        text = "Invalid price",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = raisingThreshold,
            onValueChange = {raisingThreshold = it ;newAuction.raisingThreshold = it.toDoubleOrNull(); onValueChange(newAuction) },
            label = { Text("Bid increment", fontSize = 13.sp) },
            leadingIcon = { MoneyPlusIcon() },
            singleLine = true,
            modifier = Modifier.weight(0.4f),
            isError = raisingThreshold != "" && raisingThreshold.toDoubleOrNull() == null,
            supportingText = {
                if (raisingThreshold != "" &&raisingThreshold.toDoubleOrNull() == null) {
                    Text(
                        text = "Invalid price",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
    TextField(
        value = timeInterval,
        suffix = { Text("s (${newAuction.timeInterval ?: "00:00"})") },
        onValueChange = { value ->
            timeInterval = value
            newAuction.timeInterval = value.toIntOrNull().let { if (it != null) Seconds(it) else null }
            onValueChange(newAuction)
        },
        label = { Text("Next Bid interval") },
        leadingIcon = { TimerIcon(primaryColor = MaterialTheme.colorScheme.primary)},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(screenFraction)
    )
}

@Composable
fun SilentSpecificFields(newAuction: NewAuction, onValueChange: (NewAuction) -> Unit, screenFraction: Float) {
    ExpiryDateSelector(newAuction, onValueChange, Modifier.fillMaxWidth(screenFraction))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryDateSelector(newAuction: NewAuction, onValueChange: (NewAuction) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val date = Date()
    val datePickerState = rememberDatePickerState(date.time, initialDisplayMode = DisplayMode.Input)

    TextField(
        readOnly = true,
        value = Date(datePickerState.selectedDateMillis!!).toYYYYFormattedDate(),
        onValueChange = { onValueChange(newAuction) },
        label = { Text("Expiry Date") },
        trailingIcon = {
            CalendarIcon(
                modifier = Modifier
                    .clickable { showDialog = true }
                    .padding(16.dp),
                primaryColor = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    )



    if(showDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
                datePickerState.displayMode = DisplayMode.Picker
                newAuction.expirationDate = Date(datePickerState.selectedDateMillis!!)
                onValueChange(newAuction)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        datePickerState.displayMode = DisplayMode.Picker
                        newAuction.expirationDate = Date(datePickerState.selectedDateMillis!!)
                        onValueChange(newAuction)
                    }
                ) {
                    Text("Select")
                }
            }
        ) {
            DatePicker(state = datePickerState.let { it.displayMode = DisplayMode.Picker; it })
        }
    }
}

@Composable
fun AddTagPill(primaryColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .height(30.dp)
            .background(primaryColor)
            .wrapContentWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "Add",
            color = Color.White,
            modifier = Modifier
                .padding(end = 16.dp),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun AddPhotosLazyRow(
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
) {
    val galleryLauncher = getGalleryLauncher(newAuction, onValueChange)

    val context = LocalContext.current
    val file = context.createImageFile()
    val uriForIntent = FileProvider.getUriForFile(
        Objects.requireNonNull(LocalContext.current),
        context.packageName + ".provider", file
    )
    val cameraLauncher = getCameraLauncher(newAuction, uriForIntent, onValueChange)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.CenterStart
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(newAuction.picturePaths.size) { index ->
                val painter = rememberAsyncImagePainter(newAuction.picturePaths[index])
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clickable {
                            newAuction.picturePaths.removeAt(index)
                            newAuction.picturePaths
                            onValueChange(newAuction)
                        }
                        .padding(8.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .border(
                            1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(3.dp)
                        ),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
            if (newAuction.picturePaths.size < NewAuction.MAX_PHOTOS) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                    ) {
                        CameraIconButton(
                            {
                                cameraLauncher.launch(uriForIntent)
                            }, Modifier
                                .padding(8.dp)
                                .size(120.dp, 35.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        GalleryIconButton(
                            {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }, Modifier
                                .padding(8.dp)
                                .size(120.dp, 35.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }

                }
            }

        }
    }
}

@Preview
@Composable
fun Test() {
    Text(
        text = "SUCCESS",
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        textAlign = TextAlign.Center,
        color = Color.White
    )
}




