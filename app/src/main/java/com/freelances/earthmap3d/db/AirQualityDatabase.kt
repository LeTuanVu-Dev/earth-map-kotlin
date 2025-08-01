package com.freelances.earthmap3d.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ModelAirQualityDatabase::class], version = 1, exportSchema = false)
abstract class AirQualityDatabase : RoomDatabase() {

    abstract fun airQualitySearchDao(): AirQualityDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AirQualityDatabase? = null

        fun getDatabase(context: Context): AirQualityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AirQualityDatabase::class.java,
                    "freelances_air_quality_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
