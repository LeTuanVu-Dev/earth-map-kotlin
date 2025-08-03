package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.onTextChange
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive.CameraLiveActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog.CoinDialog
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.FamousDetailActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview.CameraLivePreviewActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.purchase.PurchaseActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityFamousBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FamousActivity : BaseActivity<ActivityFamousBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityFamousBinding {
        return ActivityFamousBinding.inflate(layoutInflater)
    }

    private val famousLoader: FamousLoader by inject()
    private var filteredList = arrayListOf<ModelFamousPlace>()
    private var dataList = mutableListOf<ModelFamousPlace>()
    private var currentItem: ModelFamousPlace? = null

    private val famousAdapter by lazy {
        FamousAdapter { item ->
            currentItem = item
            showDialogUseCoin()
        }
    }

    private fun showDialogUseCoin() {
        if (!coinDialog.isAdded) coinDialog.show(
            supportFragmentManager,
            CameraLiveActivity::class.java.name
        )
    }

    private val coinDialog by lazy {
        CoinDialog().apply {
            setOnClick {
                if (preferenceHelper.coinNumber >= 50) {
                    postValueCoinAndMinusCoin()
                    currentItem?.let {
                        openPreview(it)
                    }
                } else {
                    navigateTo(PurchaseActivity::class.java)
                }
            }
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        famousLoader.loadFamousPlacesFromAssets(this)
        initData()
        binding.apply {

            ivBack.safeClick {
                finish()
            }

            ivClear.click {
                if (edtSearch.text.toString().trim().isEmpty()) return@click
                edtSearch.setText("")
                filterWithSearch(edtSearch.text.toString().trim())
            }

            edtSearch.onTextChange { string ->
                filterWithSearch(string.toString().trim())
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            famousLoader.famousResults.collect {
                if (it.isNotEmpty()) {
                    dataList.clear()
                    dataList.addAll(it)
                    famousAdapter.submitList(dataList.toList())
                }
            }
        }
        binding.rcvWorldClock.adapter = famousAdapter

    }

    private fun openPreview(item: ModelFamousPlace) {
        navigateTo(
            FamousDetailActivity::class.java, bundleOf(
                ARG_DATA to item
            )
        )
    }

    private fun filterWithSearch(query: String) {
        binding.ivClear.isVisible = query.isNotEmpty()
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(dataList)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in dataList) {
                if (item.name.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        binding.lnEmpty.isVisible = filteredList.isEmpty()
        binding.rcvWorldClock.isVisible = filteredList.isNotEmpty()
        famousAdapter.submitList(filteredList.toMutableList())
    }
}