package com.example.onehealth.domain.model.remote

import com.example.onehealth.domain.core.DomainMappable
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType

data class MeasurementData(
    val value: Double,
    val measurementType: String,
    val timeStamp: Long,
    val userId: String
): DomainMappable<MeasurementModel> {

    override fun toDomainModel() = MeasurementModel(
        value = value,
        measurementType = MeasurementType.valueOf(measurementType),
        timeStamp = timeStamp
    )

    companion object {
        fun fromModel(
            measurementModel: MeasurementModel,
            userId: String
        ): MeasurementData {
            return MeasurementData(
                value = measurementModel.value,
                measurementType = measurementModel.measurementType.name,
                timeStamp = measurementModel.timeStamp,
                userId = userId
            )
        }
    }
}