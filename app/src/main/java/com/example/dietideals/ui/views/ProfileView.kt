package com.example.dietideals.ui.views

import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dietideals.R
import com.example.dietideals.domain.auxiliary.ProfileForm
import com.example.dietideals.domain.models.Auctioneer
import com.example.dietideals.domain.models.Buyer
import com.example.dietideals.domain.models.Links
import com.example.dietideals.domain.models.User
import com.example.dietideals.ui.ProfileEditState
import com.example.dietideals.ui.ProfileFetchState
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.AddSocialsDialog
import com.example.dietideals.ui.components.EditWebsiteDialog
import com.example.dietideals.ui.components.FacebookIcon
import com.example.dietideals.ui.components.GalleryIconButton
import com.example.dietideals.ui.components.InstagramIcon
import com.example.dietideals.ui.components.LoadingView
import com.example.dietideals.ui.components.NetworkErrorView
import com.example.dietideals.ui.components.TwitterIcon
import com.example.dietideals.ui.components.UnderLine
import com.example.dietideals.ui.components.getGalleryLauncher

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
        UserLinksRows(user.links, isOwnProfile)
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
fun OtherProfileView(
    profileFetchState: ProfileFetchState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    when (profileFetchState) {
        is ProfileFetchState.Loading -> LoadingView(modifier.fillMaxSize())
        is ProfileFetchState.Error -> NetworkErrorView(modifier.fillMaxSize(), onRetry)
        is ProfileFetchState.ProfileSuccess -> {
            UserProfileView(profileFetchState.user, false, modifier)
        }
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
fun UserLinksRows(links: Links?, isOwnProfile: Boolean, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Socials: ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable(
                                enabled = !isOwnProfile
                            ) {

                            }
                    )
                }
                if(links != null){
                    Log.d("ProfileView", links.toString())
                    val map = links.toLinksMap()
                    item{
                        if (map.containsKey("instagram")) {
                            InstagramIcon(
                                modifier = Modifier
                                    .clickable {

                                    }
                            )
                        }
                    }
                    item{
                        if (map.containsKey("twitter")) {

                            TwitterIcon(
                                Modifier.clickable {

                                }
                            )
                        }
                    }
                    item{
                        if (map.containsKey("facebook")) {
                            FacebookIcon(
                                Modifier.clickable {

                                }
                            )
                        }
                    }
                }
            }
        }
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Website: ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.internet_ic),
                contentDescription = null,
                tint = if (links?.website == null) Color.Gray else Color.Black,
                modifier = Modifier.size(25.dp)
            )
            if(links?.website == null) {
                Text(text = "No website", color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
            } else {
                Text(
                    text = links.website,
                    style = LocalTextStyle.current.copy(
                        color = Color(0xFF0A81E9)
                    ),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {

                        }
                )
            }
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
fun EditProfileView(
    userState: UserState,
    profileEditState: ProfileEditState,
    modifier: Modifier = Modifier,
    onValueChange: (ProfileForm) -> Unit
) {
    val user: User = when (userState) {
        is UserState.Bidder -> userState.buyer
        is UserState.Vendor -> userState.auctioneer
        else -> throw IllegalArgumentException("User is not logged in")
    }

    val profileForm = profileEditState.profileForm
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.Top)
    ) {
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditProfilePicture(
                profileForm = profileForm,
                onValueChange = onValueChange,
                picturePath = user.proPicPath,
                modifier = Modifier.size(100.dp)
            )
            EditProfileConstantFields(user)
        }
        EditProfileLinks(modifier, profileForm, onValueChange)
        TextField(
            value = profileForm.bio.value ?: "",
            onValueChange = { profileForm.bio.value = it; onValueChange(profileForm) },
            label = {Text("Bio")},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        profileForm.bio.value = null
                        onValueChange(profileForm)
                    }
                )
            },
            singleLine = false,
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 8.dp)
                .padding(end = 64.dp)
        )
        TextField(
            value = profileForm.nationality.value?: "",
            onValueChange = { profileForm.nationality.value = it; onValueChange(profileForm) },
            label = {Text("Nationality")},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        profileForm.nationality.value = null
                        onValueChange(profileForm)
                    }
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(end = 64.dp)
        )
    }
}

@Composable
private fun EditProfileLinks(
    modifier: Modifier = Modifier,
    profileForm: ProfileForm,
    onValueChange: (ProfileForm) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {


        EditSocials(modifier, profileForm, onValueChange) {selected, social ->
            profileForm.addSocial(selected, social)
        }
        EditWebsite(modifier, profileForm.website.value) {
            profileForm.website.value = it
            if (profileForm.website.isValid) {
                onValueChange(profileForm)
            }
            profileForm.website.isValid
        }
    }
}

@Composable
fun EditSocials(
    modifier: Modifier,
    profileForm: ProfileForm,
    onValueChange: (ProfileForm) -> Unit,
    onConfirm: (String, String) -> Boolean
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val absentSocials = mutableListOf("Instagram", "Twitter", "Facebook")
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Socials: ",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        )
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                )
            }
            item {
                if (profileForm.instagram.value != null) {
                    absentSocials.remove("Instagram")
                    InstagramIcon(
                        modifier = Modifier
                            .clickable {
                                profileForm.instagram.value = null
                                onValueChange(profileForm)
                            }
                    )
                }
            }
            item {
                if (profileForm.twitter.value != null) {
                    absentSocials.remove("Twitter")
                    TwitterIcon(
                        modifier = Modifier
                            .clickable {
                                profileForm.twitter.value = null
                                onValueChange(profileForm)
                            }
                    )
                }
            }
            item {
                if (profileForm.facebook.value != null) {
                    absentSocials.remove("Facebook")
                    FacebookIcon(
                        modifier = Modifier
                            .clickable {
                                profileForm.facebook.value = null
                                onValueChange(profileForm)
                            }
                    )
                }
            }

            item {
                if(absentSocials.isNotEmpty())
                    AddTagPill(
                        primaryColor = MaterialTheme.colorScheme.primary,
                        height = 25.dp
                    ) {
                        showDialog = true
                    }
            }
        }
    }

    if (showDialog) {
        AddSocialsDialog(
            absentSocials = absentSocials,
            onDismiss = { showDialog = false }) {
            selected, social ->
             val correct = onConfirm(selected, social)
            if (correct) showDialog = false
        }
    }
}

@Composable
private fun EditWebsite(
    modifier: Modifier,
    website: String?,
    onConfirm: (String) -> Boolean,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var correctUrl by rememberSaveable { mutableStateOf(true) }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Website: ",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        )
        Row (
            modifier = Modifier
                .clickable {
                    showDialog = true
                },
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
        ){
            Icon(
                painter = painterResource(id = R.drawable.internet_ic),
                contentDescription = null,
                tint = if (website == null) Color.Gray else Color.Black,
                modifier = Modifier.size(25.dp)
            )
            if (website == null) {
                NoWebSiteText()
            } else {
                EditWebSiteText(website)
            }
        }
    }

    if(showDialog) {
        EditWebsiteDialog(correctUrl = correctUrl ,onDismiss = { showDialog = false }) {
            correctUrl = onConfirm(it)
            if(correctUrl) showDialog = false
        }
    }
}

@Composable
private fun EditWebSiteText(website: String) {
    Text(
        text = website,
        style = LocalTextStyle.current.copy(
            color = Color(0xFF0A81E9)
        ),
        textDecoration = TextDecoration.Underline
    )
}

@Composable
private fun NoWebSiteText() {
    Text(
        text = "No website",
        color = Color.Gray,
        modifier = Modifier
            .padding(top = 2.dp)
    )
}

@Composable
private fun EditProfileConstantFields(user: User) {
    Column(
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
                else -> throw IllegalArgumentException("User is not logged in")
            },
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFF696969)
            )
        )
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

@Composable
private fun EditProfilePicture(
    profileForm: ProfileForm,
    onValueChange: (ProfileForm) -> Unit,
    picturePath: String?,
    modifier: Modifier = Modifier
) {
    val restUrl = stringResource(R.string.restapi_url)
    Box(
        contentAlignment = Alignment.Center
    ) {
        if(picturePath != null){
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
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(profileForm.photo)
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
        val galleryLauncher = getGalleryLauncher(profileForm, onValueChange)
        GalleryIconButton(
            {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }, Modifier
                .align(Alignment.TopEnd)
                .size(25.dp, 25.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
        )
    }

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