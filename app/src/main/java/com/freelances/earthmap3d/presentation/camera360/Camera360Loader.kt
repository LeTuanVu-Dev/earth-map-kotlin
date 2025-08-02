package com.freelances.earthmap3d.presentation.camera360

import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class Camera360Loader {
    // StateFlow để UI có thể collect
    private val _cameraResults = MutableStateFlow<List<ModelCamera360>>(emptyList())
    val cameraResults: StateFlow<List<ModelCamera360>> = _cameraResults

    fun loadImageCamera360FromAssets(activity: BaseActivity<*>) {
        activity.lifecycleScope.launch {
            val wrapper = getListDataCamera360(activity)
            _cameraResults.value = wrapper
        }
    }
}
