package com.example.onehealth.domain.repository

import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.model.local.Period
import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {

    suspend fun flowLastMeasurements(
        measurementType: MeasurementType,
        numberOfMeasurementsToBeRetrieved: Int
    ): Flow<List<MeasurementModel>>

    suspend fun getMeasurementsBetween(
        measurementType: MeasurementType,
        period: Period,
        maxNumberOfMeasurementsToBeRetrieved: Int
    ): List<MeasurementModel>

    suspend fun saveMeasurement(measurement: MeasurementModel)
}