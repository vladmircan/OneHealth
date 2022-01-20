package com.example.onehealth.app.add_measurement

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.onehealth.R
import com.example.onehealth.app.core.components.GenericProgressIndicator

@Composable
fun AddMeasurementScreen(
    viewModel: AddMeasurementViewModel
) {
    val isLoading = viewModel.isLoading.collectAsState(false)

    GenericProgressIndicator(isLoadingState = isLoading)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val inputValue = rememberSaveable { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        InputField(
            inputValue = inputValue.value,
            label = R.string.provide_input_value,
            focusManager = focusManager
        ) {
            inputValue.value = it
        }

        SubmitButton {
            viewModel.saveMeasurement(inputValue.value)
        }
    }
}

@Composable
fun InputField(
    inputValue: String,
    @StringRes label: Int,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = inputValue,
        onValueChange = onValueChange,
        label = { stringResource(label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.submit))
    }
}