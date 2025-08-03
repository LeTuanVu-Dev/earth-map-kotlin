package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail

import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_CITY
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.GeoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Step 3: Repository class to fetch city isoCode
class CityCodeFetcher {

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_DATA_CITY)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(GeoApiService::class.java)

    suspend fun getCityIsoCode(latitude: Double, longitude: Double, cityName: String): String? {
        return try {
            val response = apiService.reverseGeocode(latitude, longitude)
            val cityItem = response.localityInfo.administrative.find {
                it.name.equals(cityName, ignoreCase = true) && it.isoCode != null
            }
            cityItem?.isoCode ?: response.principalSubdivisionCode
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
