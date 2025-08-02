package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelCamera360(
    val id: String,
    val name: String,
    val image: String
) : Parcelable

data class ModelCameraDataWrapper(
    val data: List<ModelCamera360>
)