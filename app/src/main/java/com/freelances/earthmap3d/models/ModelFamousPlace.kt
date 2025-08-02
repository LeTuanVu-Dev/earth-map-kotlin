package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelFamousPlace(
    val id: String,
    val name: String,
    val info: String,
    val history: String,
    val latitude: Double,
    val longitude: Double,
    val travels: List<ModelTravels>
): Parcelable


data class ModelFamousPlaceDataWrapper(
    val data: List<ModelFamousPlace>
)

@Parcelize
data class ModelTravels(
    val id: String,
    val name: String,
    val imageUrl: String
): Parcelable