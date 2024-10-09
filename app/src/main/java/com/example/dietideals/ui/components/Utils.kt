package com.example.dietideals.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dietideals.domain.auxiliary.NewAuction
import com.example.dietideals.domain.auxiliary.ProfileForm
import java.io.File
import java.util.Date


fun Date.toFormattedDateTime() : String {
    val year = this.year - 100
    val month = (this.month + 1).toString().padStart(2, '0')
    val day = this.date.toString().padStart(2, '0')
    val hour = this.hours.toString().padStart(2, '0')
    val minute = this.minutes.toString().padStart(2, '0')
    return "$day/$month/$year $hour:$minute"
}

fun Date.toFormattedDate() : String {
    val year = this.year - 100
    val month = (this.month + 1).toString().padStart(2, '0')
    val day = this.date.toString().padStart(2, '0')
    return "$day/$month/$year"
}

fun Date.toYYYYFormattedDate() : String {
    val year = (this.year + 1900)
    val month = (this.month + 1).toString().padStart(2, '0')
    val day = this.date.toString().padStart(2, '0')
    return "$day/$month/$year"
}

fun Color.darken(factor: Float): Color {
    return Color(
        red = this.red * (1 - factor),
        green = this.green * (1 - factor),
        blue = this.blue * (1 - factor),
        alpha = this.alpha
    )
}


@Composable
fun UnderLine(primaryColor: Color, length: Dp, thickness: Dp = 0.75.dp, distance: Dp = 2.dp) {
    Spacer(modifier = Modifier.height(distance))
    Spacer(
        modifier = Modifier
            .size(width = length, height = thickness)
            .background(color = primaryColor)
    )
}

@Composable
fun UnderLine(primaryColor: Color, modifier: Modifier = Modifier, thickness: Dp = 0.75.dp, distance: Dp = 4.dp) {
    Spacer(modifier = Modifier.height(distance))
    Spacer(
        modifier = modifier
            .height(thickness)
            .fillMaxWidth()
            .background(color = primaryColor)
    )
}

fun Context.createImageFile(): File {
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
fun getCameraLauncher(
    newAuction: NewAuction,
    uriForIntent: Uri,
    onValueChange: (NewAuction) -> Unit,
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) {
    if (it) {
        newAuction.picturePaths.add(uriForIntent)
        onValueChange(newAuction)
    } else {
        Log.d("Camera", "Picture not saved")
    }
}

@Composable
fun getGalleryLauncher(
    newAuction: NewAuction,
    onValueChange: (NewAuction) -> Unit,
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
) { uri: Uri? ->
    if (uri != null) {
        newAuction.picturePaths.add(uri)
        onValueChange(newAuction)
    }
}

@Composable
fun getGalleryLauncher(
    profileForm: ProfileForm,
    onValueChange: (ProfileForm) -> Unit,
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
) { uri: Uri? ->
    if (uri != null) {
        profileForm.photo = uri
        onValueChange(profileForm)
    }
}