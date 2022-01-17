package com.example.onehealth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onehealth.data.database.OneHealthRoomDatabase.Companion.SCHEMA_VERSION
import com.example.onehealth.data.database.entities.MeasurementEntity

@Database(
    entities = [
        MeasurementEntity::class,
    ],
    version = SCHEMA_VERSION,
    exportSchema = false
)
abstract class OneHealthRoomDatabase: RoomDatabase() {

    abstract fun measurementDao(): MeasurementDao

    companion object {
        const val SCHEMA_VERSION = 1
    }
}