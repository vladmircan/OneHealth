package com.example.onehealth.data.repository

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.data.sync.FirebaseModule
import com.example.onehealth.domain.repository.MeasurementRepository
import com.example.onehealth.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return FirebaseUserRepository()
    }

    @Singleton
    @Provides
    fun provideMeasurementRepository(
        measurementDao: MeasurementDao,
        @FirebaseModule.MeasurementCollection
        collection: CollectionReference,
        userRepository: UserRepository
    ): MeasurementRepository {
        return MeasurementRepositoryImpl(
            measurementDao,
            collection,
            userRepository
        )
    }
}