package com.example.onehealth.app.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.core.components.GenericProgressIndicator
import com.example.onehealth.app.navigation.Screen

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val email: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val password: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val isLoggedIn = viewModel.isLoggedIn.collectAsState(false)
    val isLoading = viewModel.isLoading.collectAsState(false)
    val focusManager = LocalFocusManager.current

    if (isLoggedIn.value == true) {
        navController.navigate(Screen.Home.baseRoute)
    }

    GenericProgressIndicator(isLoadingState = isLoading)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.75f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(stringResource(R.string.login_prompt))

        EmailInputField(
            value = email.value,
            onValueChange = { email.value = it },
            focusManager
        )

        PasswordInputField(
            label = R.string.password_placeholder,
            value = password.value,
            onValueChange = { password.value = it },
            imeAction = ImeAction.Done,
            focusManager = focusManager
        )

        LoginButton {
            viewModel.login(email.value, password.value)
            focusManager.clearFocus()
        }

        ToRegisterButton {
            navController.navigate(Screen.Register.baseRoute)
        }
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.login))
    }
}

@Composable
fun ToRegisterButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.register))
    }
}