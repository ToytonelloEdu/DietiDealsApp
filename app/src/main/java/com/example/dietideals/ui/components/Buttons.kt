package com.example.dietideals.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dietideals.R
import com.example.dietideals.domain.auxiliary.Gender
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.AppView
import com.example.dietideals.domain.models.Auction
import com.example.dietideals.domain.models.IncrementalAuction
import com.example.dietideals.domain.models.SilentAuction
import com.example.dietideals.domain.auxiliary.Seconds
import com.example.dietideals.domain.auxiliary.UserType
import com.example.dietideals.domain.models.Buyer
import java.text.NumberFormat


internal val routesByButton = mapOf(
    "Home" to listOf(AppView.Home, AppView.AuctionDetails),
    "Gavel" to listOf(AppView.Auctions, AppView.Bids, AppView.MyAuctionDetails, AppView.MyBidAuctionDetails, AppView.NewAuction),
    "NetUser" to listOf(AppView.Profile, AppView.LogIn, AppView.SignUp)
)


@Composable
fun AuctionButton(
    currentView: AppView,
    userState: UserState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected = (routesByButton["Gavel"]?.contains(currentView) == true)
    val isLoggedIn = (userState !is UserState.NotLoggedIn)
    Button(
        onClick = { onClick() },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = isLoggedIn && !isSelected
        ) {
        AuctionIcon(modifier, isSelected, isLoggedIn)
    }
}



@Composable
fun HomeButton(
    currentView: AppView,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected = (routesByButton["Home"]?.contains(currentView) == true)
    Button(
        onClick = { onClick() },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = !isSelected
    )  {
        HomeIcon(modifier, isSelected)
    }
}



@Composable
fun UserButton(
    currentView: AppView,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected = (routesByButton["NetUser"]?.contains(currentView) == true)
    Button(
        onClick = { onClick() },
        colors = buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = !isSelected
    )  {
        UserIcon(modifier, isSelected)
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        AuctionButton(
            currentView = AppView.Home,
            UserState.Bidder(
                Buyer(
                    username = "ciro",
                    email = "john.mckinley@examplepetstore.com",
                    password = "1234567890",
                    firstName = "Ciro",
                    lastName = "Anastasio",
                    proPicPath = "ciroanastasio.jpg",
                    bio = "FNS",
                    nationality = "Ita",
                    gender = "Male",
                    birthdate = null,
                    bids = emptyList()
                )
            )
        ) {}
        HomeButton(currentView = AppView.Home) {}
        UserButton(currentView = AppView.Home) {}
    }

}

@Composable
fun BidIconButton(
    auction: IncrementalAuction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    timeInterval: Int? = null,
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .defaultMinSize(minHeight = 45.dp)
            .background(primaryColor.let { if (auction.isAuctionOver()) it.darken(0.4f) else it })
            .clip(RoundedCornerShape(3.dp))
            .clickable(enabled = !auction.isAuctionOver()) { onAuctionClicked(auction, true) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            MoneyPlusIcon(primaryColor = Color.White)
            Text(
                text = NumberFormat.getCurrencyInstance().format(auction.raisingThreshold),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(end = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (timeInterval != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clockplus_ic),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                )
                Text(
                    text = Seconds(timeInterval).toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(start = 2.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun BidIconButton(
    auction: SilentAuction,
    primaryColor: Color,
    onAuctionClicked: (Auction, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .size(160.dp, 45.dp)
            .background(primaryColor.let { if (auction.isAuctionOver()) it.darken(0.4f) else it })
            .border(0.dp, Color.Transparent, RoundedCornerShape(3.dp))
            .clickable(enabled = !auction.isAuctionOver()) { onAuctionClicked(auction, true) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Make secret offer",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SubmitButton(
    auction: SilentAuction,
    primaryColor: Color,
    offerInput: String,
    modifier: Modifier = Modifier,
    onSubmit: (Auction, Double) -> Unit
) {
    Row(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .size(100.dp, 45.dp)
            .background(primaryColor.let { if (auction.isAuctionOver()) it.darken(0.4f) else it })
            .border(0.dp, Color.Transparent, RoundedCornerShape(3.dp))
            .clickable(enabled = !auction.isAuctionOver()) {
                onSubmit(
                    auction,
                    offerInput.toDoubleOrNull() ?: -1.0
                )
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "SUBMIT",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun LogInButton(onLogIn: () -> Unit, modifier: Modifier = Modifier, primaryColor: Color = MaterialTheme.colorScheme.primary) {
    Box (
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .size(100.dp, 40.dp)
            .background(primaryColor)
            .clickable { onLogIn() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Log In", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}

@Composable
fun SignUpButton(onSignUp: () -> Unit, modifier: Modifier = Modifier, primaryColor: Color = MaterialTheme.colorScheme.primary) {
    Column(
        modifier = modifier
            .size(100.dp, 40.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            color = primaryColor,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(2f, 2f),
                    blurRadius = 16f
                )
            ),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onSignUp() }
        )
    }

}

@Composable
fun CancelButton(onCancel: () -> Unit, modifier: Modifier = Modifier, text: String = "Cancel", primaryColor: Color = MaterialTheme.colorScheme.tertiary) {
    Box (
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .size(100.dp, 40.dp)
            .background(primaryColor)
            .clickable { onCancel() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onTertiary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}

@Composable
fun ConfirmButton(onConfirm: () -> Unit, modifier: Modifier = Modifier, text: String = "Confirm", primaryColor: Color = MaterialTheme.colorScheme.primary) {
    Box (
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(3.dp))
            .size(100.dp, 40.dp)
            .background(primaryColor)
            .clickable { onConfirm() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    genderValue: Gender,
    onValueChange: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
        val options = listOf(Gender.Male, Gender.Female, Gender.Other("Other"))
        val icons = listOf<@Composable () -> Unit>(
            { MLetterIcon() }, { FLetterIcon() },
            { Text(text ="Other", fontWeight = FontWeight.SemiBold) }
        )
        Text("Gender:")
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .clip(RoundedCornerShape(3.dp)),
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                    colors = SegmentedButtonDefaults.colors().copy(
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    onClick = { selectedIndex = index; onValueChange(option) },
                    selected = (index == selectedIndex),
                    icon = {}
                ) { icons[index].invoke() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeSelector(
    userTypeValue: UserType,
    onValueChange: (UserType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("NetUser type:", Modifier.fillMaxWidth())
        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
        val options = listOf(UserType.Auctioneer, UserType.Buyer)
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index, options.size),
                    colors = SegmentedButtonDefaults.colors().copy(
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    onClick = { selectedIndex = index; onValueChange(option) },
                    selected = (index == selectedIndex),
                    icon = {}
                ) {
                    Text(text = option.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}


@Composable
fun VisibilityButton(passwordVisible: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        when (passwordVisible) {
            true -> OpenEyeIcon(primaryColor = Color.Black)
            false -> ClosedEyeIcon(primaryColor = Color.Black)
        }
    }

}

@Composable
fun ExpandButton(expanded: Boolean, onClick: () -> Unit, primaryColor: Color) {
    Icon(
        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = primaryColor,
        modifier = Modifier
            .size(16.dp)
            .clickable { onClick() }
    )
}

@Composable
fun AcceptButton(onAccept: () -> Unit, primaryColor: Color) {
    Box(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(2.dp))
            .size(90.dp, 25.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(primaryColor)
            .clickable { onAccept() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Accept",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun GalleryIconButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
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
fun CameraIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.camera_ic),
            contentDescription = "Add from gallery",
            tint = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}


@Composable
fun NotifIconButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = R.drawable.notifs_ic),
            contentDescription = "Notifications",
            tint = color,
            modifier = Modifier.size(30.dp).padding(4.dp)
        )
    }
}

@Composable
fun SearchIconButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search_ic),
            contentDescription = "Notifications",
            tint = color,
            modifier = Modifier.size(30.dp).padding(4.dp)
        )
    }
}

@Composable
fun LogoutIconButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logout_ic),
            contentDescription = "Notifications",
            tint = color,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SettingsIconButton(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.settings_ic),
            contentDescription = "Notifications",
            tint = color,
            modifier = Modifier.padding(4.dp)
        )
    }
}