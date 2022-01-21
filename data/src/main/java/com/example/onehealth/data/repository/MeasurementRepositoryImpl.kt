package com.example.onehealth.data.repository

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.model.local.Period
import com.example.onehealth.domain.model.remote.MeasurementData
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
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

    override suspend fun getMostRecentMeasurementTime(userId: String): Long? {
        return kotlin.runCatching {
            measurementDao.getMostRecentMeasurement(userId).timeStamp
        }.getOrNull()
    }

    override suspend fun flowLastMeasurements(
        measurementType: MeasurementType,
        numberOfMeasurementsToBeRetrieved: Int
    ): Flow<List<MeasurementModel>> {
        return measurementDao.flowLastMeasurements(
            userRepository.currentUserId!!,
            measurementType,
            numberOfMeasurementsToBeRetrieved
        ).map {
            it.map { measurementEntity -> measurementEntity.toDomainModel() }.asReversed()
        }
    }

    override suspend fun getMeasurementsBetween(
        measurementType: MeasurementType,
        period: Period,
        maxNumberOfMeasurementsToBeRetrieved: Int
    ): List<MeasurementModel> {
        return measurementDao.getMeasurements(
            userId = userRepository.currentUserId!!,
            measurementType = measurementType,
            periodStartTime = period.startTime,
            periodEndTime = period.endTime,
            maxNumberOfMeasurementsToBeRetrieved = maxNumberOfMeasurementsToBeRetrieved
        ).map { entity ->
            entity.toDomainModel()
        }.asReversed()
    }

    override suspend fun saveMeasurement(measurement: MeasurementModel) {
        val listener: ListenerRegistration
        val data = MeasurementData.fromModel(measurement, userRepository.currentUserId!!)
        suspendCoroutine<Unit> { continuation ->
            val newDocumentReference = collection.document()
            listener = newDocumentReference.addSnapshotListener { document, error ->
                error?.let(continuation::resumeWithException)
                document?.reference?.set(data)
                continuation.resume(Unit)
            }
        }
        listener.remove()
    }
}