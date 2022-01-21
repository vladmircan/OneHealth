package com.example.onehealth.data.error_handling

import com.example.onehealth.data.BuildConfig
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.error_handling.ExceptionTracker

internal class ExceptionTrackerImpl(
    private val providerSpecificExceptionTracker: ExceptionTracker
): ExceptionTracker {

    override suspend fun trackFailure(failure: Failure) {
        if (BuildConfig.DEBUG)
            return
        when (failure) {
            is Failure.GenericFailure, Failure.TimeoutFailure -> {
                providerSpecificExceptionTracker.trackFailure(failure)
            }
            else -> { /*Don't track client side exceptions*/
            }
        }
    }
}