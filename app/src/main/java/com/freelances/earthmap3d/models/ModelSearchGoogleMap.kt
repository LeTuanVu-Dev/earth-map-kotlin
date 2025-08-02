package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelSearchGoogleMap(
    val title: String,
    val nameFull: String,
    val lat: String,
    val lon: String
) : Parcelable
