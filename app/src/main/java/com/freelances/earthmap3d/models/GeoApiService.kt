package com.freelances.earthmap3d.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Step 1: Retrofit interface
interface GeoApiService {
    @GET("data/reverse-geocode-client")
    suspend fun reverseGeocode(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): GeoResponse
}

interface WeatherApiService {
    @GET("weather/api/weathers")
    suspend fun getWeather(
        @Query("cityCode") cityCode: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): Response<WeatherResponse>
}

interface AirQualityApiService {
    @GET("feed/{city}/")
    suspend fun getAirQualityByCity(
        @Path("city", encoded = true) cityName: String,
        @Query("token") token: String
    ): Response<AirQualityResponse>

    @GET("feed/{geo}/")
    suspend fun getAirQualityByGeo(
        @Path("geo", encoded = true) geo: String,
        @Query("token") token: String
    ): Response<AirQualityResponse>
}

