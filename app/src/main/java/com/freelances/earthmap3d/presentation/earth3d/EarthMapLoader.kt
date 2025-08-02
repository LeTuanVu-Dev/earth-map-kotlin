package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_NOMINATIM_SEARCH
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_CONNECT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_READ
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_FORMAT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_RESULT_LIMIT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataSuggestNormal
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelSearchGoogleMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class EarthMapLoader {
    private val _searchResults = MutableStateFlow<List<ModelSearchGoogleMap>>(emptyList())
    val searchResults: StateFlow<List<ModelSearchGoogleMap>> = _searchResults

    private val client = OkHttpClient.Builder()
        .connectTimeout(API_TIMEOUT_CONNECT, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(API_TIMEOUT_READ, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(API_TIMEOUT_READ, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    fun searchNominatim(activity: BaseActivity<*>,appName: String,query: String) {
        if (query.isBlank()) return
        val queryString = "q=${query}"
        val formatSearch = "&format=$NOMINATIM_FORMAT"
        val resultLimit = "&limit=$NOMINATIM_RESULT_LIMIT"

        activity.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = API_NOMINATIM_SEARCH + queryString + formatSearch + resultLimit
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
                            val name = obj.getString("display_name")
                            val nameShort = name.split(",").firstOrNull()?.trim() ?: name
                            val lat = obj.getString("lat")
                            val lon = obj.getString("lon")
                            ModelSearchGoogleMap(nameShort,name, lat, lon)
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
}
