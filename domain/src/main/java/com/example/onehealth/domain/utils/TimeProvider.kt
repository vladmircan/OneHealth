package com.example.onehealth.domain.utils

import javax.inject.Inject
import javax.inject.Singleton

interface TimeProvider {
    fun getTimeInMillis(): Long
}

@Singleton
class TimeProviderImpl @Inject constructor(): TimeProvider {
    override fun getTimeInMillis(): Long {
        return System.currentTimeMillis()
    }
}