package com.freelances.earthmap3d.models


data class WeatherResponse(
    val statusCode: Int,
    val code: String,
    val message: String,
    val path: String,
    val timestamp: String,
    val data: WeatherData
)

data class WeatherData(
    val time: String,
    val current: CurrentWeather,
    val forecast: List<Forecast>
)

data class CurrentWeather(
    val tempC: Double,
    val tempF: Double,
    val isDay: Int,
    val windDegree: Int,
    val windDir: String,
    val precipMm: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslikeC: Double,
    val feelslikeF: Double,
    val dewpointC: Double,
    val dewpointF: Double,
    val visKm: Double,
    val uv: Double,
    val windMps: Double,
    val pressureHPa: Int,
    val conditionCode: Int,
    val airQualityIndex: Int,
    val airQualityText: String
)

data class Forecast(
    val day: DayForecast,
    val hour: List<HourlyForecast>
)

data class DayForecast(
    val maxtempC: Double,
    val maxtempF: Double,
    val mintempC: Double,
    val mintempF: Double,
    val avgtempC: Double,
    val avgtempF: Double,
    val totalprecipMm: Double,
    val totalsnowCm: Double,
    val avgvisKm: Double,
    val avghumidity: Double,
    val dailyWillItRain: Int,
    val dailyChanceOfRain: Int,
    val dailyWillItSnow: Int,
    val dailyChanceOfSnow: Int,
    val uv: Double,
    val maxwindMps: Double,
    val sunrise: String,
    val sunset: String,
    val airQualityIndex: Int,
    val airQualityText: String,
    val conditionCode: Int
)

data class HourlyForecast(
    val tempC: Double,
    val tempF: Double,
    val isDay: Int,
    val windDegree: Int,
    val windDir: String,
    val snowCm: Double,
    val humidity: Int,
    val cloud: Int,
    val dewpointC: Double,
    val dewpointF: Double,
    val willItRain: Int,
    val chanceOfRain: Int,
    val willItSnow: Int,
    val chanceOfSnow: Int,
    val visKm: Double,
    val uv: Double,
    val windMps: Double,
    val airQualityIndex: Int,
    val airQualityText: String,
    val conditionCode: Int,
    val pressureHPa: Int,
    val time: String,
    val timeEpoch: Long
)

data class ForecastDisplayItem(
    val dayOfWeek: String, // ex: "Today", "Wed"
    val dateDisplay: String, // ex: "19/05"
    val minTemp: String,
    val maxTemp: String,
    val conditionCode: Int
)

enum class WeatherType {
    CLEAR, CLOUDY, RAINY, SNOWY, FOGGY, STORMY, UNKNOWN
}
