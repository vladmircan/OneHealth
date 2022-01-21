package com.example.onehealth.data.sync

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.data.database.entities.MeasurementEntity
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.model.local.MeasurementType
import com.example.onehealth.domain.sync.SyncManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

internal class FirebaseSyncManager(
    private val measurementDao: MeasurementDao,
    private val measurementCollection: CollectionReference
): SyncManager, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + AppDispatchers.DEFAULT + CoroutineExceptionHandler { _, throwable ->
            Timber.e(
                throwable
            )
        }
    private var listener: ListenerRegistration? = null

    override fun sync() {
        launch(AppDispatchers.IO) { syncMeasurements() }
    }

    private fun syncMeasurements() {
        val mostRecentMeasurementTime = kotlin.runCatching {
            measurementDao.getMostRecentMeasurement().timeStamp
        }.getOrDefault(0)
        listener = measurementCollection
            .orderBy("timeStamp")
            .startAt(mostRecentMeasurementTime)
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
        job.cancelChildren()
        listener?.remove()
        listener = null
    }

    private fun MeasurementEntity.Companion.fromDocumentSnapshot(
        documentSnapshot: DocumentSnapshot
    ) = MeasurementEntity(
        id = documentSnapshot.id,
        value = documentSnapshot["value"] as Double,
        measurementType = MeasurementType.valueOf(documentSnapshot["measurementType"] as String),
        timeStamp = documentSnapshot["timeStamp"] as Long
    )
}