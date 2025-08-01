package com.freelances.earthmap3d.di

import android.content.Context
import android.content.SharedPreferences
import com.freelances.earthmap3d.extensions.utils.API_DATA_WEATHER
import com.freelances.earthmap3d.extensions.utils.API_DATA_AIR
import com.freelances.earthmap3d.extensions.utils.TOKEN_API_AIR
import com.freelances.earthmap3d.extensions.PreferenceHelper
import com.freelances.earthmap3d.models.AirQualityApiService
import com.freelances.earthmap3d.models.WeatherApiService
import com.freelances.earthmap3d.presentation.air.GetAirQuality
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            PreferenceHelper.PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }
    singleOf(::PreferenceHelper)

    // Retrofit singleton
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(API_DATA_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Weather API service
    single<WeatherApiService> {
        get<Retrofit>().create(WeatherApiService::class.java)
    }

    // Inject fetchers
//    singleOf(::WeatherFetcher)
//    singleOf(::CityCodeFetcher)


    single {
        Retrofit.Builder()
            .baseUrl(API_DATA_AIR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create(AirQualityApiService::class.java)
    }
    single { GetAirQuality(get(), TOKEN_API_AIR) }
}
