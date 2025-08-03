package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions

import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirColorSet
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirNote

object AirUtils {
    fun getColorSetForAqi(aqi: Int): AirColorSet {
        return when (aqi) {
            in 0..50 -> AirColorSet(R.color.color_good, R.color.white)
            in 51..100 -> AirColorSet(R.color.color_moderate, R.color.black)
            in 101..150 -> AirColorSet(R.color.color_unhealthy_for_sensitive, R.color.white)
            in 151..200 -> AirColorSet(R.color.color_unhealthy, R.color.white)
            in 201..300 -> AirColorSet(R.color.color_very_unhealthy, R.color.white)
            else -> AirColorSet(R.color.color_hazardous, R.color.white)
        }
    }

    val listNoteAir = listOf(
        AirNote(
            icon = R.drawable.ic_air_good,
            value = R.string.air_0_50,
            state = R.string.air_good
        ),
        AirNote(
            icon = R.drawable.ic_air_moderate,
            value = R.string.air_51_100,
            state = R.string.air_moderate
        ),
        AirNote(
            icon = R.drawable.ic_air_unhealty_for_sensitive,
            value = R.string.air_101_150,
            state = R.string.air_unhealthy_for_Sensitive
        ),
        AirNote(
            icon = R.drawable.ic_air_unhealthy,
            value = R.string.air_151_200,
            state = R.string.air_unhealthy
        ),
        AirNote(
            icon = R.drawable.ic_air_very_unhealthy,
            value = R.string.air_201_300,
            state = R.string.air_very_unhealthy
        ),
        AirNote(
            icon = R.drawable.ic_air_hazardous,
            value = R.string.air_300,
            state = R.string.air_hazardous
        ),
    )

}