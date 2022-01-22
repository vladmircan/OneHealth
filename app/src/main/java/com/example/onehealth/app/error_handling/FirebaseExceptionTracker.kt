package com.example.onehealth.app.error_handling

import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.error_handling.ExceptionTracker
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseExceptionTracker: ExceptionTracker {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override suspend fun trackFailure(failure: Failure) {
        crashlytics.recordException(failure.exception)
    }
}