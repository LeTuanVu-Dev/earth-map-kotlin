package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.world_clock

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_NOMINATIM_SEARCH
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_CONNECT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_TIMEOUT_READ
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_FORMAT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.NOMINATIM_RESULT_LIMIT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.TIME_12H_WITH_AMPM
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataSuggestNormal
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelSearchGoogleMap
import com.freelances.earthmap3d.models.WorldClockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WorldClockLoader {
    private val _clockItems = MutableStateFlow<List<WorldClockItem>>(emptyList())
    val clockItems: StateFlow<List<WorldClockItem>> = _clockItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    fun loadWorldClockList(activity: BaseActivity<*>) {
        activity.lifecycleScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val now = Date()
            val formatter = SimpleDateFormat(TIME_12H_WITH_AMPM, Locale.getDefault())

            val clocks = TimeZone.getAvailableIDs().mapNotNull { id ->
                val tz = TimeZone.getTimeZone(id)
                val parts = id.split("/")
                if (parts.size >= 2) {
                    val city = parts[1].replace("_", " ")
                    val region = parts[0]
                    formatter.timeZone = tz
                    val time = formatter.format(now)

                    val offsetMillis = tz.rawOffset
                    val hours = offsetMillis / 3600000
                    val minutes = (offsetMillis % 3600000) / 60000
                    val gmtOffset = String.format("GMT %+02d:%02d", hours, minutes)

                    WorldClockItem(city, region, time, gmtOffset)
                } else null
            }.distinctBy { "${it.city}-${it.region}" }

            _clockItems.value = clocks
            _isLoading.value = false
        }
    }
}
