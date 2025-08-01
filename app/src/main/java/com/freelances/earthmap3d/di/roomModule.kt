package com.freelances.earthmap3d.di

import android.app.Application
import androidx.room.Room
import com.freelances.earthmap3d.db.AirQualityDatabase
import com.freelances.earthmap3d.db.AirQualityDatabaseDao
import com.freelances.earthmap3d.db.AirQualityRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            AirQualityDatabase::class.java,
            "freelances_air_quality_database.db"
        ).build()
    }

    single<AirQualityDatabaseDao> { get<AirQualityDatabase>().airQualitySearchDao() }

    singleOf(::AirQualityRepository)
}
