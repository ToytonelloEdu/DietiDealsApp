package com.example.dietideals.ui.components

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