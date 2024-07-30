package com.example.dietideals.ui.views

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.dietideals.R
import com.example.dietideals.domain.auxiliary.AuctionType
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.Seconds
import com.example.dietideals.ui.components.AuctioneerIconText
import com.example.dietideals.ui.components.CalendarIconText
import com.example.dietideals.ui.components.CancelButton
import com.example.dietideals.ui.components.ConfirmButton
import com.example.dietideals.ui.components.MoneyIcon
import com.example.dietideals.ui.components.MoneyPlusIcon
import com.example.dietideals.ui.components.TimerIcon
import com.example.dietideals.ui.components.TimerIconText
import java.io.File
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAuctionView(
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
    formInvalid: Boolean,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        AddPhotosLazyRow(newAuction, onValueChange)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp)

        )

        Column (
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TagsIconLabel(
                primaryColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                fontSize = 16.sp,
                iconSize = 14.dp,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tags = newAuction.tags
                items(tags.size) {
                    TagPill(tag = tags[it], primaryColor = MaterialTheme.colorScheme.primary)
                }
                item {
                    AddTagPill(primaryColor = MaterialTheme.colorScheme.primary) {

                    }
                }
            }
        }

        var selectedIndex: Int? by rememberSaveable {
            mutableStateOf(newAuction.auctionType?.index)
        }
        val options = listOf(AuctionType.IncrementalAuction, AuctionType.SilentAuction)
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                    0 -> { IncrementalSpecificFields(newAuction, onValueChange) }
                    1 -> { SilentSpecificFields(newAuction, onValueChange) }
                    else -> {}
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onCancel = onCancelClick)
                    ConfirmButton(onConfirm = onConfirmClick)
                }
            }
        }


    }
}

@Composable
fun IncrementalSpecificFields(newAuction: NewAuction, onValueChange: (NewAuction) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = newAuction.startingPrice.let { it?.toString() ?: "" },
            onValueChange = { newAuction.startingPrice = it.toDoubleOrNull(); onValueChange(newAuction) },
            label = { Text("Starting price", fontSize = 13.sp) },
            leadingIcon = { MoneyIcon(primaryColor = MaterialTheme.colorScheme.primary) },
            singleLine = true,
            modifier = Modifier.weight(0.4f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = newAuction.raisingThreshold.let { it?.toString() ?: "" },
            onValueChange = { newAuction.raisingThreshold = it.toDoubleOrNull(); onValueChange(newAuction) },
            label = { Text("Bid increment", fontSize = 13.sp) },
            leadingIcon = { MoneyPlusIcon() },
            singleLine = true,
            modifier = Modifier.weight(0.4f)
        )
    }
    TextField(
        value = newAuction.timeInterval.let { it?.toString() ?: "" },
        onValueChange = { newAuction.timeInterval = it.toIntOrNull().let{ intValue ->
            if (intValue != null) Seconds(intValue) else null
        }; onValueChange(newAuction) },
        label = { Text("Next Bid interval") },
        leadingIcon = { TimerIcon(primaryColor = MaterialTheme.colorScheme.primary)},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun SilentSpecificFields(newAuction: NewAuction, onValueChange: (NewAuction) -> Unit) {
    TODO("Add date picker")
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
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            newAuction.picturePaths.add(uri)
            onValueChange(newAuction)
        }
    }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uriForIntent = FileProvider.getUriForFile(
        Objects.requireNonNull(LocalContext.current),
        context.packageName + ".provider", file
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            newAuction.picturePaths.add(uriForIntent)
            onValueChange(newAuction)
        } else {
            Log.d("Camera", "Picture not saved")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
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
                        .size(135.dp, 135.dp)
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
            if (newAuction.picturePaths.size < 3) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                    ) {
                        CameraIconButton {
                            cameraLauncher.launch(uriForIntent)
                        }
                        GalleryIconButton {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    }

                }
            }

        }
    }
}

private fun Context.createImageFile(): File {
    val timeStamp = Date()
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

@Composable
private fun GalleryIconButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp, 35.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.gallery_ic),
            contentDescription = "Add from gallery",
            tint = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun CameraIconButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp, 35.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.camera_ic),
            contentDescription = "Add from gallery",
            tint = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}