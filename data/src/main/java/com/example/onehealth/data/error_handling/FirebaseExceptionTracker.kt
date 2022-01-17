package com.example.onehealth.data.error_handling

import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.error_handling.ExceptionTracker

internal class FirebaseExceptionTracker: ExceptionTracker {

    override suspend fun trackFailure(failure: Failure) {

    }
}