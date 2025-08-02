package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "air_quality",indices = [Index(value = ["geo"], unique = true)])
data class ModelAirQualityDatabase(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val nameFull: String,
    val geo: String,
    val airQuality: Int = 0
) : Parcelable
{
    companion object{
        fun String.toGeoQuery(): String = "geo:$this"
    }
}


