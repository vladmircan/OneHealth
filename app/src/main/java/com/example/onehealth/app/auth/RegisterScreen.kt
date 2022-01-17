package com.example.onehealth.app.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.onehealth.R

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val email: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val password: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val confirmPassword: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.75f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(stringResource(R.string.register_prompt))

        EmailInputField(
            value = email.value,
            onValueChange = { email.value = it },
            focusManager
        )

        PasswordInputField(
            label = R.string.password_placeholder,
            value = password.value,
            onValueChange = { password.value = it },
            imeAction = ImeAction.Next,
            focusManager = focusManager
        )

        PasswordInputField(
            label = R.string.confirm_password,
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            imeAction = ImeAction.Done,
            focusManager = focusManager
        )

        RegisterButton {
            viewModel.register(email.value, password.value, confirmPassword.value)
            focusManager.clearFocus()
        }

        ToLoginButton {
            navController.popBackStack()
        }
    }
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.register))
    }
}

@Composable
fun ToLoginButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.login))
    }
}