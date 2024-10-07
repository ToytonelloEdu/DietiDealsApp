package com.example.dietideals.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dietideals.R
import com.example.dietideals.domain.auxiliary.Seconds
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.Bid
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.models.User
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Date

@Composable
fun AuctioneerIconText(
    auction: Auction,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 160.dp,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick(auction.auctioneerUsername ?: auction.auctioneer?.username ?: "unknown") }
    ) {
        Row {
            AuctioneerIcon(primaryColor)
            Text(
                text = auction.auctioneerUsername ?: auction.auctioneer?.username ?: "Unknown",
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
        UnderLine(primaryColor, length =  underlineLength)
    }
}

@Composable
fun AuctioneerIconText(
    auctioneer: String,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 160.dp,
    underlineDistance: Dp = 2.dp,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick(auctioneer) }

    ) {
        Row {
            AuctioneerIcon(primaryColor)
            Text(
                text = auctioneer,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
        UnderLine(primaryColor, length =  underlineLength, distance = underlineDistance)
    }
}


@Composable
fun BidderIconText(
    lastBid: Bid,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 160.dp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick(lastBid.bidder ?: lastBid.buyer?.username ?: "unknown") }
    ) {
        Row {
            AuctioneerIcon(primaryColor)
            Text(
                text = lastBid.bidder ?: lastBid.buyer?.username ?: "Unknown",
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = fontWeight
            )
        }
        UnderLine(primaryColor, length =  underlineLength)
    }
}


@Composable
fun BuyerIconText(
    buyer: String,
    primaryColor: Color,
    onClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick(buyer) }
    ) {
        AuctioneerIcon(primaryColor = primaryColor)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = buyer, fontWeight = FontWeight.Medium)
    }
}




@Composable
fun DateIconText(
    date: Date,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 160.dp
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Row {
            ClockIcon(primaryColor)
            Text(
                text = date.toFormattedDateTime(),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 15.sp
            )
        }
        UnderLine(primaryColor, length =  underlineLength)
    }
}



@Composable
fun PriceIconText(
    amount: Double?,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MoneyIcon(primaryColor, Modifier.padding(horizontal = 8.dp))
            Text(
                text = NumberFormat
                    .getCurrencyInstance()
                    .format(amount) ?: "Unknown",
                fontSize = 15.sp
            )
        }
        UnderLine(primaryColor, length = 100.dp)
    }
}


@Composable
fun TimerIconText(
    auction: IncrementalAuction,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineDistance: Dp = 4.dp,
    underlineWidth: Dp = 100.dp,
    updating: Boolean = false
) {
    val remainingSeconds = auction.calculateRemainingTime()
    var remainingTime by rememberSaveable {
        mutableIntStateOf(remainingSeconds.toInt())
    }
    if(updating) {
        LaunchedEffect(remainingTime) {
            while (remainingTime > 0) {
                remainingTime--
                delay(1000L)
            }
        }
    }

    Column (
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TimerIcon(primaryColor)
            Text(
                text = "$remainingSeconds",
                fontSize = 15.sp
            )
        }
        UnderLine(primaryColor, underlineWidth , 0.75.dp, underlineDistance)
    }
}

@Composable
fun ExpirationIconText(auction: SilentAuction, primaryColor: Color) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painter = painterResource(R.drawable.timer_ic),
                contentDescription = "expiration date",
                tint = primaryColor,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(18.dp)
            )
            Text(
                text = auction.expirationDateToDate().toFormattedDate(),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        UnderLine(primaryColor, 125.dp , 0.75.dp, 4.dp)
    }
}

@Composable
fun CalendarIconText(
    auction: SilentAuction,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 125.dp,
    fontSize: TextUnit = 14.sp,
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CalendarIcon(
                primaryColor, Modifier
                    .padding(horizontal = 4.dp)
                    .size(18.dp)
            )
            Text(
                text = auction.expirationDateToDate().toFormattedDateTime(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = fontSize
            )
        }
        UnderLine(primaryColor, underlineLength , 0.75.dp, 4.dp)
    }
}

@Composable
fun CalendarIconText(
    date: Date,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    underlineLength: Dp = 125.dp,
    fontSize: TextUnit = 14.sp,
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CalendarIcon(
                primaryColor, Modifier
                    .padding(horizontal = 4.dp)
                    .size(18.dp)
            )
            Text(
                text = date.toFormattedDate(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = fontSize
            )
        }
        UnderLine(primaryColor, underlineLength , 0.75.dp, 4.dp)
    }
}

@Composable
fun EmailTextField(
    label: String,
    handleValue: String,
    onValueChange: (String) -> Unit,
    wrongCredentials: Boolean,
    textForError: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = handleValue,
        onValueChange = { onValueChange(it) },
        leadingIcon = { AtIcon(Color.Black) },
        label = { Text(label) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = wrongCredentials,
        supportingText = { if (wrongCredentials) Text(textForError) },
        modifier = modifier
    )
}

@Composable
fun PasswordTextField(
    label: String,
    passwordValue: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityClick: () -> Unit,
    wrongCredentials: Boolean,
    textForError: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done
) {
    TextField(
        value = passwordValue,
        onValueChange = { onValueChange(it) },
        leadingIcon = { StarIcon(Color.Black) },
        trailingIcon = {
            VisibilityButton(
                passwordVisible,
                modifier = Modifier.size(24.dp)
            ) { onVisibilityClick() }
        },
        label = { Text(label) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp
        ),
        visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        isError = wrongCredentials,
        supportingText = { if (wrongCredentials) Text(textForError) },
        modifier = modifier
    )
}

@Composable
fun UsernameTextField(
    label: String,
    usernameValue: String,
    onValueChange: (String) -> Unit,
    wrongCredentials: Boolean,
    textForError: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = usernameValue,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        leadingIcon = { AuctioneerIcon(primaryColor = Color.Black) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = wrongCredentials,
        supportingText = { if (wrongCredentials) Text(textForError) },
        modifier = modifier
    )
}

@Composable
fun NameTextField(
    label: String,
    nameValue: String,
    onValueChange: (String) -> Unit,
    wrongCredentials: Boolean,
    textForError: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = nameValue,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        leadingIcon = { NLetterIcon() },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = wrongCredentials,
        supportingText = { if (wrongCredentials) Text(textForError) },
        modifier = modifier,

    )
}

@Composable
fun SurnameTextField(
    label: String,
    surnameValue: String,
    onValueChange: (String) -> Unit,
    wrongCredentials: Boolean,
    textForError: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = surnameValue,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        leadingIcon = { SLetterIcon() },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = wrongCredentials,
        supportingText = { if (wrongCredentials) Text(textForError) },
        modifier = modifier
    )
}

@Composable
fun AmountIconText(amount: Double, primaryColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        MoneyIcon(primaryColor = primaryColor)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = amount.toString(), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TimePlusIconText(
    timeInterval: Int,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    fontWeight: FontWeight? = FontWeight.SemiBold,
    underLine: Boolean = false
) {
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TimePlusIcon(primaryColor)
                Text(
                    text = Seconds(timeInterval).toString(),
                    color = textColor,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(start = 2.dp),
                    fontWeight = fontWeight,
                    fontSize = 15.sp
                )
            }
            if (underLine) UnderLine(primaryColor, length = 100.dp)
        }
}