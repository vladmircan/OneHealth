package com.example.onehealth.domain.error_handling

import com.example.onehealth.domain.core.Failure

interface ExceptionTracker {

    suspend fun trackFailure(failure: Failure)
}