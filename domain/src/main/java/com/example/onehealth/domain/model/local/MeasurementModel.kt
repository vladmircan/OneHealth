package com.example.onehealth.domain.model.local

data class MeasurementModel(
    val value: Double,
    val measurementType: MeasurementType,
    val timeStamp: Long
)

enum class MeasurementType {
    BODY_WEIGHT,
    BODY_FAT_PERCENTAGE
}