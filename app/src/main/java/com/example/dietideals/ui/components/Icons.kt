package com.example.dietideals.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dietideals.R
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction

@Composable
fun colorByTheme(isLoggedIn: Boolean = true): Color {
    return if (isLoggedIn) {
        if(isSystemInDarkTheme())
            Color.White
        else
            Color.Black
    } else {
        Color.Gray
    }
}


@Composable
fun AuctionIcon(modifier: Modifier, isSelected: Boolean, isLoggedIn: Boolean) {
    Icon(
        painter = auctionIconForSelected(isSelected),
        contentDescription = "Auction",
        modifier = modifier.size(34.dp),
        tint = colorByTheme(isLoggedIn)
    )
}

@Composable
private fun auctionIconForSelected(isSelected: Boolean) : Painter {
    return if(isSelected)
        painterResource(id = R.drawable.filled_auction_ic)
    else
        painterResource(id = R.drawable.auction_ic)
}

@Composable
fun HomeIcon(modifier: Modifier, isSelected: Boolean) {
    Icon(
        painter = homeIconForSelected(isSelected),
        contentDescription = "Home",
        modifier = modifier.size(34.dp),
        tint = colorByTheme()
    )
}

@Composable
private fun homeIconForSelected(isSelected: Boolean) : Painter {
    return if(isSelected)
        painterResource(id = R.drawable.filled_home_ic)
    else
        painterResource(id = R.drawable.home_ic)
}

@Composable
fun UserIcon(modifier: Modifier, isSelected: Boolean) {
    Icon(
        painter = userIconForSelected(isSelected),
        contentDescription = "User",
        modifier = modifier.size(38.dp),
        tint = colorByTheme()
    )
}

@Composable
private fun userIconForSelected(isSelected: Boolean) : Painter {
    return if(isSelected)
        painterResource(id = R.drawable.filled_user_ic)
    else
        painterResource(id = R.drawable.user_ic)
}

@Composable
fun TagIcon(tint: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.tag_ic),
        contentDescription = null,
        modifier = modifier.size(34.dp),
        tint = tint
    )
}

@Composable
fun AuctioneerIcon(primaryColor: Color) {
    Icon(
        painter = painterResource(id = R.drawable.auctioneer_ic),
        contentDescription = null,
        tint = primaryColor
    )
}

@Composable
fun MoneyIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.money_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier
    )
}

@Composable
fun ClockIcon(primaryColor: Color) {
    Icon(
        painter = painterResource(id = R.drawable.clock_ic),
        contentDescription = null,
        tint = primaryColor
    )
}

@Composable
fun iconByAuctionType(auction: Auction) = when (auction) {
    is SilentAuction -> painterResource(id = R.drawable.silence_ic)
    is IncrementalAuction -> painterResource(id = R.drawable.british_ic)
    else -> throw IllegalArgumentException("Unknown auction type")
}

@Composable
fun AtIcon(primaryColor: Color) {
    Icon(
        painter = painterResource(id = R.drawable.at_ic),
        contentDescription = null,
        tint = primaryColor
    )
}

@Composable
fun StarIcon(primaryColor: Color) {
    Icon(
        painter = painterResource(id = R.drawable.star_ic),
        contentDescription = null,
        tint = primaryColor
    )
}

@Composable
fun ClosedEyeIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.closedeye_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun OpenEyeIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.openeye_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun NLetterIcon(modifier: Modifier = Modifier, primaryColor: Color = Color.Black) {
    Icon(
        painter = painterResource(id = R.drawable.nletter_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(20.dp)
    )
}

@Composable
fun SLetterIcon(modifier: Modifier = Modifier, primaryColor: Color = Color.Black){
    Icon(
        painter = painterResource(id = R.drawable.sletter_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(20.dp)
    )
}

@Composable
fun MLetterIcon(modifier: Modifier = Modifier, primaryColor: Color = Color.Black){
    Icon(
        painter = painterResource(id = R.drawable.mletter_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun FLetterIcon(modifier: Modifier = Modifier, primaryColor: Color = Color.Black){
    Icon(
        painter = painterResource(id = R.drawable.fletter_ic),
        contentDescription = null,
        tint = primaryColor,
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun CalendarIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.calendar_ic),
        contentDescription = "expiration date",
        tint = primaryColor,
        modifier = modifier
    )
}

@Composable
fun EditIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.edit_ic),
        contentDescription = "edit",
        tint = primaryColor,
        modifier = modifier.size(22.dp)
    )
}

@Composable
fun PreviousIcon(primaryColor: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.previous_ic),
        contentDescription = "previous",
        tint = primaryColor,
        modifier = modifier.size(22.dp)
    )
}