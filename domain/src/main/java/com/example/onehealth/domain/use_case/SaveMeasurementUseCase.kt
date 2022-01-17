package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.utils.TimeProviderImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveMeasurementUseCase @Inject constructor(
    private val measurementRepository: MeasurementRepository,
    private val timeProvider: TimeProviderImpl
): UseCase<SaveMeasurementUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val measurement = MeasurementModel(
            value = params.stringValue.toDouble(),
            measurementType = params.measurementType,
            timeStamp = timeProvider.getTimeInMillis()
        )
        measurementRepository.saveMeasurement(measurement)
    }

    data class Params(
        val stringValue: String,
        val measurementType: MeasurementType
    ): UseCase.Params
}