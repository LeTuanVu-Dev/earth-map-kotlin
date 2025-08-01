package com.freelances.earthmap3d.presentation.air

import com.freelances.earthmap3d.models.AirQualityApiService
import com.freelances.earthmap3d.models.AirQualityResponse
import com.freelances.earthmap3d.models.AirQualityResultWithOrigin

class GetAirQuality(
    private val apiService: AirQualityApiService,
    private val apiKey: String
) {
    suspend fun getAQI(cityName: String): AirQualityResponse? {
        return try {
            val response = apiService.getAirQualityByCity(cityName, apiKey)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAQIForGeo(geo: String): AirQualityResponse? {
        return try {
            val response = apiService.getAirQualityByGeo(geo, apiKey)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAQIForGeoMatchUI(geo: String): AirQualityResultWithOrigin? {
        return try {
            val response = apiService.getAirQualityByGeo(geo, apiKey)
            if (!response.isSuccessful || response.body() == null) return null
            val after = "feed/geo:"
            val before = "/?"
            val urlGeo = response.raw().request.url.toString()
                .substringAfter(after)
                .substringBefore(before)

            AirQualityResultWithOrigin(
                geoOrigin = urlGeo,
                response = response.body()!!
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

