package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.AirQualityRepository
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.ModelAirQualityDatabase
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.ModelAirQualityDatabase.Companion.toGeoQuery
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_NOMINATIM_SEARCH
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_CONNECT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_READ
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_ADDRESS_DETAIL
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_FORMAT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_RESULT_LIMIT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirData
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelAirQualitySearch
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.toDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder

class AirQualityViewModel(
    private val airQualityFetcher: AirQualityFetcher,
    private val repository: AirQualityRepository
) : ViewModel() {

    private val _currentAqi = MutableStateFlow<AirData?>(null)
    val currentAqi: StateFlow<AirData?> get() = _currentAqi

    private val _cityAqiList = MutableStateFlow<List<AirData>>(emptyList())
    val cityAqiList: StateFlow<List<AirData>> get() = _cityAqiList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _searchResults = MutableStateFlow<List<ModelAirQualitySearch>>(emptyList())
    val searchResults: StateFlow<List<ModelAirQualitySearch>> = _searchResults

    // StateFlow để cập nhật danh sách trong UI
    private val _items = MutableStateFlow<List<ModelAirQualityDatabase>>(emptyList())
    val items: StateFlow<List<ModelAirQualityDatabase>> get() = _items

    private val _hasData = MutableStateFlow(false)
    val hasData: StateFlow<Boolean> get() = _hasData

    private val topCities = listOf(
        "new york", "london", "paris", "tokyo", "beijing",
        "sydney", "singapore", "dubai", "los angeles", "hanoi"
    )

    fun loadAqiForListGeo(listGeo: List<ModelAirQualityDatabase>) {
        viewModelScope.launch {
            _isLoading.value = true

            val dbList = withContext(Dispatchers.IO) {
                listGeo.map { item ->
                    async {
                        val result = airQualityFetcher.getAQIForGeoMatchUI(item.geo.toGeoQuery())
                        result?.let { wrapper ->
                            wrapper.response.data.toDatabaseModel(wrapper.geoOrigin)
                        }
                    }
                }.awaitAll()
                    .filterNotNull()
            }

            updateAllGeo(dbList)
            _items.value = dbList
            _isLoading.value = false
        }
    }

    private fun updateAllGeo(items: List<ModelAirQualityDatabase>) {
        viewModelScope.launch {
            repository.updateListByGeo(items)
        }
    }

    private fun loadAqiForTopCities() {
        viewModelScope.launch {
            _isLoading.value = true
            val dataList = withContext(Dispatchers.IO) {
                topCities.map { city ->
                    async {
                        airQualityFetcher.getAQI(city)?.data
                    }
                }.awaitAll().filterNotNull()
            }
            _isLoading.value = false
            _cityAqiList.value = dataList
        }
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(API_TIMEOUT_CONNECT, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(API_TIMEOUT_READ, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(API_TIMEOUT_READ, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    fun searchCity(appName: String, query: String) {
        if (query.isBlank()) return

        val city = "city=${URLEncoder.encode(query, "UTF-8")}"
        val formatSearch = "&format=$NOMINATIM_FORMAT"
        val resultLimit = "&limit=$NOMINATIM_RESULT_LIMIT"
        val address = "&addressdetails=$NOMINATIM_ADDRESS_DETAIL"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = API_NOMINATIM_SEARCH + city + formatSearch + resultLimit + address
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", appName)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        val jsonArray = JSONArray(body ?: "[]")

                        val results = List(jsonArray.length()) { i ->
                            val obj = jsonArray.getJSONObject(i)
                            val nameFull = obj.optString("display_name")
                            val nameShort = nameFull.split(",").firstOrNull()?.trim() ?: nameFull
                            val lat = obj.optDouble("lat")
                            val lon = obj.optDouble("lon")
                            val geo = "$lat;$lon"

                            ModelAirQualitySearch(nameShort, nameFull, geo)
                        }
                        _searchResults.value = results
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    init {
        observeDb()
    }

    private fun observeDb() {
        viewModelScope.launch {
            repository.getAllFlow()
                .onStart {
                _isLoading.value = true
            }.collect { data ->
                    if (data.isNotEmpty()){
                        _items.value = data
                    }
                _isLoading.value = false
            }
        }
    }

    fun syncDbFromApiIfHasData() {
        viewModelScope.launch {
            val data = repository.getAll()
            if (data.isNotEmpty()) {
                val geoList = data.distinct()
                loadAqiForListGeo(geoList)
            }
        }
    }

    fun loadAqiByGeo(geo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val data = withContext(Dispatchers.IO) {
                airQualityFetcher.getAQIForGeo(geo)?.data
            }
            _isLoading.value = false
            _currentAqi.value = data
        }
    }

    fun insert(item: ModelAirQualityDatabase) {
        viewModelScope.launch {
            _isLoading.value = true
            val airData = withContext(Dispatchers.IO) {
                async {
                    val result = airQualityFetcher.getAQIForGeoMatchUI(item.geo.toGeoQuery())
                    result?.let { wrapper ->
                        wrapper.response.data.toDatabaseModel(wrapper.geoOrigin)
                    }
                }.await()
            }
            val updatedItem = item.copy(airQuality = airData?.airQuality ?: 0)
            repository.insert(updatedItem)
            _isLoading.value = false
        }
    }


    fun checkHasDataDbWithQuery() = viewModelScope.launch {
        val hasData = repository.hasData()
        _hasData.value = hasData
        if (!hasData) {
            loadAqiForTopCities()
        }
    }

}
