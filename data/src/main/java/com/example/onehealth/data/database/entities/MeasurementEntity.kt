package com.example.onehealth.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onehealth.domain.core.DomainMappable
import com.example.onehealth.domain.model.local.MeasurementModel
import com.example.onehealth.domain.model.local.MeasurementType

@Entity(tableName = "measurements")
data class MeasurementEntity(
    @PrimaryKey
    val id: String,
    val value: Double,
    @ColumnInfo(name = "measurement_type")
    val measurementType: MeasurementType,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long,
    @ColumnInfo(name = "user_id")
    val userId: String
): DomainMappable<MeasurementModel> {

    override fun toDomainModel() = MeasurementModel(
        value,
        measurementType,
        timeStamp
    )

    companion object
}