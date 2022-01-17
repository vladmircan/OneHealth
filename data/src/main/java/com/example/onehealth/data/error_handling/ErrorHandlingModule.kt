package com.example.onehealth.data.error_handling

import com.example.onehealth.domain.error_handling.ExceptionTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlingModule {

    @Singleton
    @Provides
    fun provideExceptionTracker(): ExceptionTracker {
        return ExceptionTrackerImpl(FirebaseExceptionTracker())
    }
}