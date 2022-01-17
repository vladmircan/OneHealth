package com.example.onehealth.domain.use_case

import com.example.onehealth.domain.core.UseCase
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowLastMeasurementsOfMeasurementTypeUseCase @Inject constructor(
    private val measurementRepository: MeasurementRepository
): UseCase<FlowLastMeasurementsOfMeasurementTypeUseCase.Params, Flow<List<MeasurementModel>>>() {

    override suspend fun execute(params: Params): Flow<List<MeasurementModel>> {
        return measurementRepository.flowLastMeasurements(
            params.measurementType,
            Constants.NUMBER_OF_MEASUREMENTS_IN_CHART_OVERVIEW
        )
    }

    @JvmInline
    value class Params(val measurementType: MeasurementType): UseCase.Params
}