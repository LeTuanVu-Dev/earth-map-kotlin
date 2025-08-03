package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.ModelAirQualityDatabase

fun AirData.toDatabaseModel(): ModelAirQualityDatabase? {
    val name = city.name
    val geo = "${city.geo.firstOrNull()};${city.geo.lastOrNull()}"
    if (geo.isBlank()) return null

    val nameShort = name.split(",").firstOrNull()?.trim() ?: name

    return ModelAirQualityDatabase(
        title = nameShort,
        nameFull = name,
        geo = geo,
        airQuality = aqi
    )
}

fun AirData.toDatabaseModel(geoOrigin: String): ModelAirQualityDatabase {
    val name = city.name
    val nameShort = name.split(",").firstOrNull()?.trim() ?: name
    return ModelAirQualityDatabase(
        geo = geoOrigin,
        title = nameShort,
        nameFull = city.name,
        airQuality = aqi
    )
}

