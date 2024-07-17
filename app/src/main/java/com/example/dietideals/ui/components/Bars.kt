package com.example.dietideals.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dietideals.R
import com.example.dietideals.ui.AppView
import com.example.dietideals.ui.theme.topAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentScreen: AppView ,modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = stringResource(id = currentScreen.title)) },
        colors = topAppBarColors()
    )
}

@Composable
fun AppBottomBar(currentScreen: AppView, modifier: Modifier = Modifier) {
    BottomAppBar (
        modifier = Modifier.height(66.dp),
        tonalElevation = 1.dp,

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AuctionButton(currentScreen)
                HomeButton(currentScreen)
                UserButton(currentScreen)
            }
    }
}