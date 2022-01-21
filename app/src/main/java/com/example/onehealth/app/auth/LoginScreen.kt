@file:Suppress("FunctionName")

package com.example.onehealth.app.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.core.components.GenericProgressIndicator
import com.example.onehealth.app.navigation.Screen
import com.example.onehealth.app.utils.userDisplayMessageId
import com.example.onehealth.domain.core.Failure

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val email: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val password: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val isLoggedIn = viewModel.isLoggedIn.collectAsState(false)
    val isLoading = viewModel.isLoading.collectAsState(false)
    val failureState = viewModel.failure.collectAsState(null)
    val focusManager = LocalFocusManager.current

    if (isLoggedIn.value == true) {
        navController.navigate(Screen.Home.baseRoute)
    }

    GenericProgressIndicator(isLoadingState = isLoading)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.75f)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val failure = failureState.value) {
            is Failure.AuthFailure -> Text(
                text = stringResource(failure.userDisplayMessageId!!),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center
            )
            else -> Text(
                text = stringResource(R.string.login_prompt),
                textAlign = TextAlign.Center
            )
        }

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
            navController.navigate(Screen.Register.getRoute())
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