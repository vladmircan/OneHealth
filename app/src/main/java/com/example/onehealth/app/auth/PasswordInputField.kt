@file:Suppress("FunctionName")

package com.example.onehealth.app.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.onehealth.R

@Composable
fun PasswordInputField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
        label = { Text(text = stringResource(label)) },
        onValueChange = onValueChange,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}