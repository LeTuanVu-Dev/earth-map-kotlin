package com.freelances.earthmap3d.models

// Step 2: Response data model
data class GeoResponse(
    val principalSubdivisionCode: String,
    val localityInfo: LocalityInfo
)

data class LocalityInfo(
    val administrative: List<AdminItem>
)

data class AdminItem(
    val name: String,
    val isoCode: String?
)
