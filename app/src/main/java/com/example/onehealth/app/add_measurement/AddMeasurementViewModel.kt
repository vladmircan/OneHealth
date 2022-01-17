package com.example.onehealth.app.add_measurement

import com.example.onehealth.app.core.BaseViewModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.use_case.SaveMeasurementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMeasurementViewModel @Inject constructor(
    private val saveMeasurementUseCase: SaveMeasurementUseCase
): BaseViewModel() {

    fun saveMeasurement(inputValue: String) {
        saveMeasurementUseCase.perform(
            SaveMeasurementUseCase.Params(
                stringValue = inputValue,
                measurementType = MeasurementType.BODY_WEIGHT
            )
        )
    }
}