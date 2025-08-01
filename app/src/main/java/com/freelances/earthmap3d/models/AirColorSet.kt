package com.freelances.earthmap3d.models

data class AirColorSet(
    val backgroundColorRes: Int,
    val textColorRes: Int
)

data class AirData(
    val aqi: Int,
    val idx: Int,
    val attributions: List<Attribution>,
    val city: CityInfo,
    val dominentpol: String,
    val iaqi: Iaqi,
    val time: AirTime,
    val forecast: ForecastAir?,
    val debug: DebugInfo
)

data class DebugInfo(
    val sync: String
)


data class CityInfo(
    val name: String,
    val geo: List<Double>
)

data class Attribution(
    val url: String,
    val name: String,
    val logo: String? = null
)

data class Iaqi(
    val co: IaqiValue? = null,
    val dew: IaqiValue? = null,
    val h: IaqiValue? = null,
    val no2: IaqiValue? = null,
    val p: IaqiValue? = null,
    val pm10: IaqiValue? = null,
    val pm25: IaqiValue? = null,
    val t: IaqiValue? = null,
    val w: IaqiValue? = null
)

data class IaqiValue(
    val v: Float
)

data class AirTime(
    val s: String,
    val tz: String,
    val v: Long,
    val iso: String
)

data class ForecastAir(
    val daily: DailyForecast
)

data class DailyForecast(
    val pm10: List<ForecastItem>,
    val pm25: List<ForecastItem>,
    val uvi: List<ForecastItem>
)

data class ForecastItem(
    val avg: Int,
    val day: String,
    val max: Int,
    val min: Int
)

data class AirNote(
    val icon: Int,
    val value: Int,
    val state: Int,
)


data class AirQualityResponse(
    val status: String,
    val data: AirData
)
data class AirQualityResultWithOrigin(
    val geoOrigin: String,
    val response: AirQualityResponse
)



