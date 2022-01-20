package com.example.onehealth.app.add_measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.onehealth.R
import com.example.onehealth.app.core.components.GenericProgressIndicator
import com.example.onehealth.app.utils.labelStringId
import com.example.onehealth.app.utils.unitCodeStringId
import com.example.onehealth.app.utils.userDisplayMessageId
import com.example.onehealth.domain.core.Failure

@Composable
fun AddMeasurementScreen(
    viewModel: AddMeasurementViewModel,
    navController: NavController
) {
    val isLoading = viewModel.isLoading.collectAsState(false)
    val failureState = viewModel.failure.collectAsState(null)
    val wasMeasurementSubmitted = remember { viewModel.wasMeasurementSubmitted }

    if (wasMeasurementSubmitted.value) {
        navController.popBackStack()
    }

    GenericProgressIndicator(isLoadingState = isLoading)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val inputValue = rememberSaveable { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        when (val failure = failureState.value) {
            is Failure.MeasurementFailure -> Text(
                text = stringResource(failure.userDisplayMessageId!!),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center
            )
            else -> Text(
                text = stringResource(
                    R.string.please_enter_your,
                    stringResource(viewModel.measurementType.labelStringId).lowercase()
                ),
                textAlign = TextAlign.Center
            )
        }

        InputField(
            inputValue = inputValue.value,
            label = stringResource(viewModel.measurementType.labelStringId),
            unitCode = stringResource(viewModel.measurementType.unitCodeStringId),
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
    label: String,
    unitCode: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(fraction = 0.75f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = { Text(text = label) },
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),

            singleLine = true,
        )
        Text(
            text = unitCode,
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        )
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.submit))
    }
}