package com.example.onehealth.app.auth

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.onehealth.R

@Composable
fun EmailInputField(
    value: String,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager
) {
    Patterns.EMAIL_ADDRESS
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = stringResource(R.string.email_placeholder)) },
        label = { Text(text = stringResource(R.string.email_placeholder)) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}