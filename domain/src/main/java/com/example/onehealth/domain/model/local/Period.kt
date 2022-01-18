package com.example.onehealth.domain.model.local

import com.example.onehealth.domain.utils.formatTimeStamp

data class Period(
    val startTime: Long,
    val endTime: Long
) {
    fun format(dateFormat: String) = FormattedPeriod(
        startTime = startTime.formatTimeStamp(dateFormat),
        endTime = endTime.formatTimeStamp(dateFormat),
    )
}

data class FormattedPeriod(
    val startTime: String,
    val endTime: String
)