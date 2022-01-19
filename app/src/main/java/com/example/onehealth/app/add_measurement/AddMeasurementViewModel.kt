package com.example.onehealth.app.add_measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.use_case.SaveMeasurementUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AddMeasurementViewModel @AssistedInject constructor(
    @Assisted private val measurementType: MeasurementType,
    private val saveMeasurementUseCase: SaveMeasurementUseCase
): BaseViewModel() {

    fun saveMeasurement(inputValue: String) {
        saveMeasurementUseCase.perform(
            SaveMeasurementUseCase.Params(
                stringValue = inputValue,
                measurementType = measurementType
            )
        )
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