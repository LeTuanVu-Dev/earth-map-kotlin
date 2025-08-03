package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelAirQualitySearch(
    val title: String,
    val nameFull: String,
    val geo: String,
) : Parcelable
