package com.example.onehealth.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onehealth.data.database.entities.MeasurementEntity
import com.example.onehealth.domain.model.local.MeasurementType
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {

    @Query("Select * from measurements where measurement_type = :measurementType and time_stamp >= :periodStartTime and time_stamp <= :periodEndTime limit :maxNumberOfMeasurementsToBeRetrieved")
    fun getMeasurements(
        measurementType: MeasurementType,
        periodStartTime: Long,
        periodEndTime: Long,
        maxNumberOfMeasurementsToBeRetrieved: Int
    ): List<MeasurementEntity>

    @Query("Select * from measurements where measurement_type = :measurementType limit :numberOfMeasurementsToBeRetrieved")
    fun flowLastMeasurements(
        measurementType: MeasurementType,
        numberOfMeasurementsToBeRetrieved: Int
    ): Flow<List<MeasurementEntity>>

    @Query("Select * from measurements order by time_stamp desc limit 1")
    fun getMostRecentMeasurement(): MeasurementEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMeasurements(measurements: List<MeasurementEntity>)
}