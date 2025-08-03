package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.Forecast
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ForecastDisplayItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object WeatherUtils {
    fun getWeatherIconRes(conditionCode: Int, isDay: Int): Int {
        return when (conditionCode) {
            1000 -> if (isDay == 1) R.drawable.ic_sunny else R.drawable.ic_weather_cloud
            in 1003..1009 -> if (isDay == 1) R.drawable.ic_sunny else R.drawable.ic_weather_cloud
            in 1063..1195 -> R.drawable.ic_rain
            in 1210..1237 -> R.drawable.ic_snowy
            else -> R.drawable.ic_foggy
        }
    }


    fun getWeatherType(conditionCode: Int): Int {
        return when (conditionCode) {
            in 1000..1009 -> R.drawable.ic_sunny
            in 1063..1195 -> R.drawable.ic_rain
            in 1210..1237 -> R.drawable.ic_snowy
            else -> R.drawable.ic_foggy
        }
    }

    fun getWeatherCurrentState(conditionCode: Int): Int {
        val description = when (conditionCode) {
            in 1000..1009 -> R.string.sunny
            in 1063..1195 -> R.string.rain
            in 1210..1237 -> R.string.snowy
            else -> R.string.foggy
        }
        return description
    }

    fun mapForecastToDisplay(
        forecast: List<Forecast>,
        baseTime: String
    ): List<ForecastDisplayItem> {
        val baseDate = LocalDate.parse(baseTime.substring(0, 10)) // ex: 2025-05-23
        val today = LocalDate.now()

        return forecast.mapIndexed { index, forecastItem ->
            val date = baseDate.plusDays(index.toLong())
            val dayOfWeek = when {
                date == today -> "Today"
                else -> date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ) // e.g., "Wed"
            }

            ForecastDisplayItem(
                dayOfWeek = dayOfWeek,
                dateDisplay = date.format(DateTimeFormatter.ofPattern("dd/MM")),
                minTemp = "${forecastItem.day.mintempC}°C",
                maxTemp = "${forecastItem.day.maxtempC}°C",
                conditionCode = forecastItem.day.conditionCode
            )
        }
    }
}