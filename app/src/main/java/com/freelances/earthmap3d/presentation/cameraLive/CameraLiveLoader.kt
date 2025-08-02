package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive

import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataSuggestNormal
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CameraLiveLoader {
    // StateFlow để UI có thể collect
    private val _cameraResults = MutableStateFlow<List<LocationModel>>(emptyList())
    val cameraResults: StateFlow<List<LocationModel>> = _cameraResults

    fun loadDataLiveFromAssets(activity: BaseActivity<*>) {
        activity.lifecycleScope.launch {
            val wrapper = getListDataSuggestNormal(activity)
            _cameraResults.value = wrapper
        }
    }
}
