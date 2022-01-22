package com.example.onehealth.data

import android.content.Context
import androidx.room.Room
import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.data.database.OneHealthRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DataAndroidTestModule {

    @Provides
    @Named("testDatabase")
    fun provideDatabase(@ApplicationContext context: Context): OneHealthRoomDatabase {
        return Room.inMemoryDatabaseBuilder(context, OneHealthRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Named("testMeasurementsDao")
    fun provideMeasurementsDao(
        @Named("testDatabase")
        oneHealthRoomDatabase: OneHealthRoomDatabase
    ): MeasurementDao {
        return oneHealthRoomDatabase.measurementDao()
    }
}