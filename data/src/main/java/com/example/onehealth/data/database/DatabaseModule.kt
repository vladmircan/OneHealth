package com.example.onehealth.data.database

import android.content.Context
import androidx.room.Room
import com.example.onehealth.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): OneHealthRoomDatabase {
        val builder =
            Room.databaseBuilder(context, OneHealthRoomDatabase::class.java, "onehealth.db")
        if (!BuildConfig.DEBUG) {
            val factory = SupportFactory(SQLiteDatabase.getBytes("PassPhrase".toCharArray()))
            builder.openHelperFactory(factory)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideMeasurementDao(database: OneHealthRoomDatabase): MeasurementDao {
        return database.measurementDao()
    }
}