package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AirQualityDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: ModelAirQualityDatabase)

    @Query("SELECT * FROM air_quality ORDER BY id DESC")
    suspend fun getAll(): List<ModelAirQualityDatabase>

    @Query("SELECT * FROM air_quality ORDER BY id DESC")
    fun getAllFlow(): Flow<List<ModelAirQualityDatabase>>

    @Query("DELETE FROM air_quality")
    suspend fun clearAll()

    @Delete
    suspend fun delete(search: ModelAirQualityDatabase)

    @Query("SELECT EXISTS(SELECT 1 FROM air_quality LIMIT 1)")
    suspend fun hasAnyData(): Boolean

    @Update
    suspend fun update(search: ModelAirQualityDatabase)

    @Query(
        """
        UPDATE air_quality 
        SET airQuality = :aqi
        WHERE geo = :geo
    """
    )
    suspend fun updateByGeo(
        geo: String,
        aqi: Int
    )

    @Transaction
    suspend fun bulkUpdateByGeo(list: List<ModelAirQualityDatabase>) {
        list.forEach {
            val affected = updateByGeo(it.geo, it.airQuality)
        }
    }
}
