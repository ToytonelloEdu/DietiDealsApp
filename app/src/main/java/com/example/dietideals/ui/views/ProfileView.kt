package com.example.dietideals.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dietideals.R
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.User
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.UnderLine

@Composable
fun ProfileView(userState: UserState, modifier: Modifier = Modifier, isOwnProfile: Boolean = true) {
    val user: User = when (userState) {
        is UserState.Bidder -> userState.buyer
        is UserState.Vendor -> userState.auctioneer
        else -> throw IllegalArgumentException("NetUser is not logged in")
    }
    UserProfileView(user, isOwnProfile, modifier)
}

@Composable
fun UserProfileView(user: User, isOwnProfile: Boolean, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.Top)
    ) {
        UserMainInfosRow(user)
        UserLinksRows(user, isOwnProfile)
        UserFieldBox("Bio", user.bio ?: "No bio",
            Modifier
                .wrapContentHeight(),
                boxMinHeight = 100.dp,
                boxAlignment = Alignment.TopStart
            )
        UserFieldBox("Nationality", user.nationality ?: "No specified nationality",
            Modifier
                .wrapContentHeight(),
                fontSize = 20.sp
            )
    }
}

@Composable
fun UserMainInfosRow(user: User, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(user.proPicPath, Modifier.size(100.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            )
            UnderLine(
                primaryColor = MaterialTheme.colorScheme.primary,
                length = 225.dp,
                thickness = 1.dp,
                distance = 0.dp
            )
            Text(
                text = when (user) {
                    is Buyer -> "Buyer"
                    is Auctioneer -> "Auctioneer"
                    else -> throw IllegalArgumentException("NetUser is not logged in")
                },
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF696969)
                )
            )
        }
    }
}

@Composable
fun UserLinksRows(user: User, isOwnProfile: Boolean, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Socials:")

        }
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Website:")

        }
    }

}

@Composable
fun UserFieldBox(
    label: String,
    field: String,
    modifier: Modifier = Modifier,
    boxMinHeight: Dp = 42.dp,
    boxAlignment: Alignment = Alignment.CenterStart,
    fontSize: TextUnit = 17.sp,
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(end = 64.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text= label,
            modifier = Modifier.padding(start = 2.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 21.sp
            )
        )
        UnderLine(
            primaryColor = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            distance = 2.dp
        )
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = boxMinHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = boxAlignment
        ) {
            Text(
                text = field,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = fontSize,
                    color = Color(0xFF393939)
                )
            )
        }
    }
}

@Composable
private fun ProfilePicture(picturePath: String?, modifier: Modifier = Modifier) {
    val restUrl = stringResource(R.string.restapi_url)
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data("${restUrl}photos/$picturePath")
            .build(),
        contentDescription = "profile picture",
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.loading_img),
        error = painterResource(id = R.drawable.propicplaceholder),
        modifier = modifier
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileViewPreview() {
    ProfileView(
        UserState.Vendor(
            Auctioneer(
                username = "toytonello",
                email = "ascionantonio@gmail.com",
                password = null,
                firstName = "Antonio",
                lastName = "Ascione",
                proPicPath = "toytonello.jpg",
                bio = "Sono uno studente di Informatica alla Federico II.\nHo 22 anni e amo la natura",
                nationality = "Italia",
                gender = "Male",
                birthdate = null,
                auctions = mutableListOf()
            )
        )
    )
}