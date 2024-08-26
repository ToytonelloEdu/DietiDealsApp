package com.example.dietideals.ui.views

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dietideals.domain.auxiliary.NewUser
import com.example.dietideals.ui.components.CalendarIcon
import com.example.dietideals.ui.components.CancelButton
import com.example.dietideals.ui.components.ConfirmButton
import com.example.dietideals.ui.components.EmailTextField
import com.example.dietideals.ui.components.GenderSelector
import com.example.dietideals.ui.components.NameTextField
import com.example.dietideals.ui.components.PasswordTextField
import com.example.dietideals.ui.components.SurnameTextField
import com.example.dietideals.ui.components.UserTypeSelector
import com.example.dietideals.ui.components.UsernameTextField
import com.example.dietideals.ui.components.toYYYYFormattedDate
import java.util.Date

enum class SignUpSteps{
    InsertInfo,
    InsertCredentials
}

@Composable
fun SignUpView(
    newUser: NewUser,
    onValueChange: (NewUser) -> Unit,
    formInvalid: Boolean,
    onCancelClick: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SignUpSteps.InsertInfo.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = modifier.fillMaxSize()
    ) {
        composable(SignUpSteps.InsertInfo.name) {
            InsertInfoView(
                newUser = newUser,
                onValueChange = onValueChange,
                formInvalid = formInvalid,
                onCancelClick = onCancelClick,
                onNextClick = { navController.navigate(SignUpSteps.InsertCredentials.name) }
            )
        }
        composable(SignUpSteps.InsertCredentials.name) {
            InsertCredentialsView(
                newUser = newUser,
                onValueChange = onValueChange,
                formInvalid = formInvalid,
                onBackClick = { navController.navigate(SignUpSteps.InsertInfo.name) },
                onSignupClick = onSignupClick
            )
        }

    }
}



@Composable
fun InsertInfoView(
    newUser: NewUser,
    onValueChange: (NewUser) -> Unit,
    formInvalid: Boolean = false,
    onCancelClick: () -> Unit,
    onNextClick: () -> Unit,
    screenFraction: Float = 0.75f
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column (
            Modifier.fillMaxWidth(screenFraction),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Insert your informations",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            UsernameTextField(
                label = "Username",
                usernameValue = newUser.username.value,
                onValueChange = { newUser.username.value = it; onValueChange(newUser) },
                wrongCredentials = newUser.username.value != "" && !newUser.username.isValid,
                textForError = newUser.username.message,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            )
            NameTextField(
                label = "Name",
                nameValue = newUser.firstName.value,
                onValueChange = { newUser.firstName.value = it; onValueChange(newUser) },
                wrongCredentials = newUser.firstName.value != "" && !newUser.firstName.isValid,
                textForError = newUser.firstName.message,
                modifier = Modifier.fillMaxWidth()
            )
            SurnameTextField(
                label = "Surname",
                surnameValue = newUser.lastName.value,
                onValueChange = { newUser.lastName.value = it; onValueChange(newUser) },
                wrongCredentials = newUser.lastName.value != "" && !newUser.lastName.isValid,
                textForError = newUser.lastName.message,
                modifier = Modifier.fillMaxWidth()
            )
        }
        GenderSelector(
            genderValue = newUser.gender.value,
            onValueChange = { newUser.gender.value = it; onValueChange(newUser) },
            Modifier.fillMaxWidth(screenFraction)
        )
        BirthdateSelector(
            newUser.birthdate.value,
            onValueChange = { newUser.birthdate.value = it; onValueChange(newUser) },
            modifier = Modifier
                .fillMaxWidth(screenFraction)
        )
        UserTypeSelector(
            userTypeValue = newUser.userType.value,
            onValueChange = { newUser.userType.value = it; onValueChange(newUser) },
            modifier = Modifier.fillMaxWidth(screenFraction)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(screenFraction),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CancelButton(onCancel = onCancelClick)
            ConfirmButton(onConfirm = onNextClick, text = "Next", enabled = newUser.firstHalfValid)
        }
    }
}


@Composable
fun InsertCredentialsView(
    newUser: NewUser,
    onValueChange: (NewUser) -> Unit,
    formInvalid: Boolean = false,
    onBackClick: () -> Unit,
    onSignupClick: () -> Unit,
    screenFraction: Float = 0.75f
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        Text(
            text = "Insert your credentials",
            modifier = Modifier
                .fillMaxWidth(screenFraction),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        EmailTextField(
            label = "Email",
            handleValue = newUser.email.value,
            onValueChange = { newUser.email.value = it; onValueChange(newUser) },
            wrongCredentials = newUser.email.value != "" && !newUser.email.isValid,
            textForError = newUser.email.message,
            modifier = Modifier.fillMaxWidth(screenFraction)
        )
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PasswordTextField(
                label = "Password",
                passwordValue = newUser.password.value,
                onValueChange = { newUser.password.value = it; onValueChange(newUser) },
                passwordVisible = passwordVisible,
                onVisibilityClick = { passwordVisible = !passwordVisible },
                wrongCredentials = newUser.password.value != "" && !newUser.password.isValid,
                textForError = newUser.password.message,
                Modifier.fillMaxWidth(screenFraction),
                imeAction = ImeAction.Next
            )
            PasswordTextField(
                label = "Confirm password",
                passwordValue = newUser.passwordConfirm.value,
                onValueChange = { newUser.passwordConfirm.value = it; onValueChange(newUser) },
                passwordVisible = passwordVisible,
                onVisibilityClick = { passwordVisible = !passwordVisible },
                wrongCredentials = newUser.passwordConfirm.value != "" && !newUser.passwordConfirm.isValid,
                textForError = newUser.passwordConfirm.message,
                Modifier.fillMaxWidth(screenFraction)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(screenFraction),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CancelButton(onCancel = onBackClick, text = "Back")
            ConfirmButton(onConfirm = onSignupClick, enabled = newUser.secondHalfValid)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdateSelector(
    birthdate: Date,
    onValueChange: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        var showDialog by rememberSaveable { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState(
            birthdate.time,
            initialDisplayMode = DisplayMode.Input
        )
        Text("Birthdate:")
        Row (
            Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { showDialog = true },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Date(datePickerState.selectedDateMillis!!).toYYYYFormattedDate(),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            CalendarIcon(
                primaryColor = MaterialTheme.colorScheme.primary, modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        }



        if(showDialog) {
            DatePickerDialog(
                onDismissRequest = {
                    showDialog = false
                    datePickerState.displayMode = DisplayMode.Picker
                    onValueChange(Date(datePickerState.selectedDateMillis!!))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            datePickerState.displayMode = DisplayMode.Picker
                            onValueChange(Date(datePickerState.selectedDateMillis!!))
                        }
                    ) {
                        Text("Select")
                    }
                }
            ) {
                DatePicker(state = datePickerState.let { it.displayMode = DisplayMode.Picker; it })
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpViewPreview() {
    SignUpView(onSignupClick = {}, onCancelClick = {}, newUser = NewUser(), onValueChange = {_ ->}, formInvalid = false)
}