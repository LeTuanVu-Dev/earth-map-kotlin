package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val videoLink: String
):Parcelable

data class LocationDataWrapper(
    val data: List<LocationModel>
)
