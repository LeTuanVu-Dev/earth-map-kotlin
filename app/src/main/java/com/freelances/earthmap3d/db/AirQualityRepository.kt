package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db

import kotlinx.coroutines.flow.Flow

class AirQualityRepository(private val dao: AirQualityDatabaseDao) {

    suspend fun insert(search: ModelAirQualityDatabase) = dao.insert(search)

    suspend fun getAll() = dao.getAll()

    fun getAllFlow(): Flow<List<ModelAirQualityDatabase>> = dao.getAllFlow()

    suspend fun clearAll() = dao.clearAll()

    suspend fun delete(search: ModelAirQualityDatabase) = dao.delete(search)

    suspend fun hasData(): Boolean = dao.hasAnyData()

    suspend fun updateListByGeo(list: List<ModelAirQualityDatabase>) {
        dao.bulkUpdateByGeo(list)
    }


}
