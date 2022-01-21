package com.example.onehealth.data.error_handling

import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.error_handling.ExceptionTracker
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal class FirebaseExceptionTracker: ExceptionTracker {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override suspend fun trackFailure(failure: Failure) {
        crashlytics.recordException(failure.exception)
    }
}