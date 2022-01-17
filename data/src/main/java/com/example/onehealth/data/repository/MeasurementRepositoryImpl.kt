package com.example.onehealth.data.repository

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.repository.MeasurementData
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class MeasurementRepositoryImpl(
    private val measurementDao: MeasurementDao,
    private val collection: CollectionReference,
    private val userRepository: UserRepository
): MeasurementRepository {
    override suspend fun flowLastMeasurements(
        measurementType: MeasurementType,
        numberOfMeasurementsToBeRetrieved: Int
    ): Flow<List<MeasurementModel>> {
        return measurementDao.flowLastMeasurements(
            measurementType,
            numberOfMeasurementsToBeRetrieved
        ).map {
            it.map { measurementEntity -> measurementEntity.toDomainModel() }
        }
    }

    override suspend fun getMeasurementsBetween(
        measurementType: MeasurementType,
        periodStartTime: Long,
        periodEndTime: Long,
        maxNumberOfMeasurementsToBeRetrieved: Int
    ) {
        measurementDao.getMeasurements(
            measurementType = measurementType,
            periodStartTime = periodStartTime,
            periodEndTime = periodEndTime,
            maxNumberOfMeasurementsToBeRetrieved = maxNumberOfMeasurementsToBeRetrieved
        )
    }

    override suspend fun saveMeasurement(
        measurement: MeasurementModel
    ) = suspendCoroutine<Unit> { continuation ->
        collection
            .add(MeasurementData.fromModel(measurement, userRepository.user!!.userId!!))
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }
}