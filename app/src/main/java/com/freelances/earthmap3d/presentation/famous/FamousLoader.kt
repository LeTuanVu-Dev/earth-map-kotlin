package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getListDataFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FamousLoader {
    private val _famousResults = MutableStateFlow<List<ModelFamousPlace>>(emptyList())
    val famousResults: StateFlow<List<ModelFamousPlace>> = _famousResults

    fun loadFamousPlacesFromAssets(activity: BaseActivity<*>) {
        activity.lifecycleScope.launch(Dispatchers.IO) {
            val wrapper = getListDataFamousPlace(activity)
            _famousResults.value = wrapper
        }
    }
}
