package com.example.onehealth.data.sync

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.data.database.entities.MeasurementEntity
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.model.local.UserModel
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.sync.SyncManager
import com.example.onehealth.domain.utils.collectInScope
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

internal class FirebaseSyncManager(
    private val measurementRepository: MeasurementRepository,
    private val measurementDao: MeasurementDao,
    private val measurementCollection: CollectionReference,
    private val userRepository: UserRepository
): SyncManager, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + AppDispatchers.DEFAULT + CoroutineExceptionHandler { _, throwable ->
            Timber.e(
                throwable
            )
        }
    private val syncJobs = mutableListOf<Job>()
    private val listenerRegistrations = mutableListOf<ListenerRegistration>()

    override fun start() {
        userRepository.userFlow.collectInScope(this, AppDispatchers.IO) { currentUser ->
            this.cancel()
            if (currentUser != null)
                sync(currentUser)
        }
    }

    private fun sync(currentUser: UserModel) {
        syncJobs += launch(AppDispatchers.IO) {
            syncMeasurements(
                currentUser = currentUser,
                mostRecentMeasurementTime = measurementRepository.getMostRecentMeasurementTime(
                    currentUser.userId
                ) ?: 0
            )
        }
    }

    private fun syncMeasurements(
        currentUser: UserModel,
        mostRecentMeasurementTime: Long
    ) {
        listenerRegistrations += measurementCollection
            .whereEqualTo("userId", currentUser.userId)
            .orderBy("timeStamp")
            .startAfter(mostRecentMeasurementTime)
            .addSnapshotListener(AppDispatchers.IO.asExecutor()) { value, error ->
                launch(AppDispatchers.IO) {
                    if (error != null)
                        throw error
                    val measurementEntities = value?.documents?.map { documentSnapshot ->
                        MeasurementEntity.fromDocumentSnapshot(documentSnapshot)
                    } ?: emptyList()
                    measurementDao.addMeasurements(measurementEntities)
                }
            }
    }

    override fun cancel() {
        syncJobs.forEach(Job::cancel)
        listenerRegistrations.forEach(ListenerRegistration::remove)
    }
}

private fun MeasurementEntity.Companion.fromDocumentSnapshot(
    documentSnapshot: DocumentSnapshot
) = MeasurementEntity(
    id = documentSnapshot.id,
    value = documentSnapshot["value"] as Double,
    measurementType = MeasurementType.valueOf(documentSnapshot["measurementType"] as String),
    timeStamp = documentSnapshot["timeStamp"] as Long,
    userId = documentSnapshot["userId"] as String
)