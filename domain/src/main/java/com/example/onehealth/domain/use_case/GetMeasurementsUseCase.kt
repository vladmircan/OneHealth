package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.model.local.Period
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMeasurementsUseCase @Inject constructor(
    private val measurementRepository: MeasurementRepository
): UseCase<GetMeasurementsUseCase.Params, List<MeasurementModel>>() {

    override suspend fun execute(params: Params): List<MeasurementModel> {
        return measurementRepository.getMeasurementsBetween(
            measurementType = params.measurementType,
            period = params.period,
            maxNumberOfMeasurementsToBeRetrieved = Constants.CHART_MEASUREMENTS_CAPACITY
        )
    }

    data class Params(
        val measurementType: MeasurementType,
        val period: Period
    ): UseCase.Params
}