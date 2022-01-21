package com.example.onehealth.data.sync

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.repository.UserRepository
import com.example.onehealth.domain.sync.SyncManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @MeasurementCollection
    fun provideMeasurementCollectionReference(): CollectionReference {
        return Firebase.firestore.collection(MEASUREMENT_COLLECTION_PATH)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MeasurementCollection

    @Provides
    @Singleton
    fun provideSyncManager(
        measurementRepository: MeasurementRepository,
        measurementDao: MeasurementDao,
        @MeasurementCollection
        measurementCollection: CollectionReference,
        userRepository: UserRepository
    ): SyncManager {
        return FirebaseSyncManager(
            measurementRepository,
            measurementDao,
            measurementCollection,
            userRepository
        )
    }

    private const val MEASUREMENT_COLLECTION_PATH = "measurements"
}