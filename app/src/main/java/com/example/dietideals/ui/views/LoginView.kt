package com.example.dietideals.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dietideals.ui.UserState
import com.example.dietideals.ui.components.EmailTextField
import com.example.dietideals.ui.components.LogInButton
import com.example.dietideals.ui.components.PasswordTextField
import com.example.dietideals.ui.components.SignUpButton

@Composable
fun LogInView(
    loginState: UserState.NotLoggedIn,
    onLoginClick: (String, String) -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ThirdPartyLogin()
        InAppLogin(loginState.wrongCredentials, onLoginClick, onSignupClick)
    }
}

@Composable
fun InAppLogin(wrongCredentials: Boolean, onLoginClick: (String, String) -> Unit, onSignupClick: () -> Unit, modifier: Modifier = Modifier) {
    var handleValue by rememberSaveable { mutableStateOf("") }
    var passwordValue by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column (
        modifier = modifier
            .fillMaxWidth(0.75f)
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(text = "Already signed up?")
            Text(
                text = "Insert your credentials:",
                fontSize = 22.sp,
                softWrap = false,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            EmailTextField(
                "Email/Username",
                handleValue,
                {handleValue = it},
                wrongCredentials,
                "Wrong credentials",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            PasswordTextField(
                "Password",
                passwordValue,
                { passwordValue = it },
                passwordVisible,
                { passwordVisible = !passwordVisible},
                wrongCredentials,
                "Wrong credentials",
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .padding(horizontal = 8.dp)
        ) {
            SignUpButton(onSignupClick)
            LogInButton({ Log.i("LogInView", "$handleValue $passwordValue") ;onLoginClick(handleValue, passwordValue) })
        }
    }
}

@Composable
fun ThirdPartyLogin() {
    Column {
        Text(text = "ThirdParty Logins", modifier = Modifier.height(64.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogInViewPreview() {
    LogInView(onLoginClick = { _, _ ->}, onSignupClick = {}, loginState = UserState.NotLoggedIn(false))
}
