package com.example.onehealth.app.add_measurement

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.use_case.SaveMeasurementUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber

class AddMeasurementViewModel @AssistedInject constructor(
    @Assisted val measurementType: MeasurementType,
    private val saveMeasurementUseCase: SaveMeasurementUseCase
): BaseViewModel() {

    val wasMeasurementSubmitted = mutableStateOf(false)

    fun saveMeasurement(inputValue: String) {
        saveMeasurementUseCase.perform(
            input = SaveMeasurementUseCase.Params(
                stringValue = inputValue,
                measurementType = measurementType
            ),
            onSuccess = {
                wasMeasurementSubmitted.value = true
            }
        )
    }

    override fun handleFailure(failure: Failure) {
        when (failure) {
            is Failure.MeasurementFailure -> {
                Timber.e(failure.exception)
                viewModelScope.launch { _failure.emit(failure) }
            }
            else -> super.handleFailure(failure)
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(measurementType: MeasurementType): AddMeasurementViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            measurementType: MeasurementType,
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(measurementType) as T
            }
        }
    }
}