package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail

import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_WEATHER
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.WeatherApiService
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Step 3: Repository class to fetch city isoCode
class WeatherFetcher {

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_DATA_WEATHER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(WeatherApiService::class.java)

    suspend fun getWeather(
        cityCode: String,
        latitude: Double,
        longitude: Double
    ): WeatherResponse? {
        return try {
            val response = apiService.getWeather(cityCode, latitude, longitude)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

