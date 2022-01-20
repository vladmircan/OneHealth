package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.Failure
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
        val value = params.stringValue.toDouble()
        if (value.toInt() !in params.measurementType.valueRange()) {
            throw Failure.MeasurementFailure.MeasurementValueOutsideRange
        }

        val measurement = MeasurementModel(
            value = value,
            measurementType = params.measurementType,
            timeStamp = timeProvider.getTimeInMillis()
        )
        measurementRepository.saveMeasurement(measurement)
    }

    data class Params(
        val stringValue: String,
        val measurementType: MeasurementType
    ): UseCase.Params

    private fun MeasurementType.valueRange(): IntRange {
        return when (this) {
            MeasurementType.BODY_WEIGHT -> 0..500
            MeasurementType.BODY_FAT_PERCENTAGE -> 0..100
        }
    }
}