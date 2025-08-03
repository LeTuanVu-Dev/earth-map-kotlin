package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.di

import android.content.Context
import android.content.SharedPreferences
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_AIR
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_WEATHER
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.TOKEN_API_AIR
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirQualityApiService
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.WeatherApiService
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air.AirQualityFetcher
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive.CameraLiveLoader
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d.EarthMapLoader
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.FamousLoader
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.CityCodeFetcher
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.WeatherFetcher
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.world_clock.WorldClockLoader
import com.earthmap.map.ltv.tracker.extensions.PreferenceHelper
import com.freelances.earthmap3d.presentation.camera360.Camera360Loader
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
    singleOf(::WeatherFetcher)
    singleOf(::CityCodeFetcher)
    singleOf(::Camera360Loader)
    singleOf(::CameraLiveLoader)
    singleOf(::EarthMapLoader)
    singleOf(::WorldClockLoader)
    singleOf(::FamousLoader)

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
    single { AirQualityFetcher(get(), TOKEN_API_AIR) }
}
