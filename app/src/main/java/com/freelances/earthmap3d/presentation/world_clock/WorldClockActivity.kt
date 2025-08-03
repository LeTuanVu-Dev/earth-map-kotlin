package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.world_clock

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.hideKeyboard
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.onTextChange
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ActivityWorldClockBinding
import com.freelances.earthmap3d.models.WorldClockItem
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WorldClockActivity : BaseActivity<ActivityWorldClockBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityWorldClockBinding {
        return ActivityWorldClockBinding.inflate(layoutInflater)
    }

    private val worldClockLoader: WorldClockLoader by inject()
    private val adapter by lazy {
        WorldClockAdapter()
    }

    private var filteredList = arrayListOf<WorldClockItem>()
    private var dataList = mutableListOf<WorldClockItem>()


    override fun updateUI(savedInstanceState: Bundle?) {
        worldClockLoader.loadWorldClockList(this)
        initData()
        initAction()
    }

    private fun initData() {
        binding.rcvWorldClock.adapter = adapter
        lifecycleScope.launch {
            launch {
                worldClockLoader.isLoading.collect { loading ->
                    binding.frLoading.isVisible = loading
                }
            }

            launch {
                worldClockLoader.clockItems.collect { list ->
                    if (list.isNotEmpty()) {
                        dataList.addAll(list)
                        adapter.submitList(list)
                        binding.lnEmpty.gone()
                    } else {
                        val isLoading = worldClockLoader.isLoading.value
                        binding.lnEmpty.isVisible = !isLoading
                    }
                }
            }
        }
    }

    private fun initAction(){
        binding.apply {

            ivBack.safeClick {
                finish()
            }

            ivClear.click {
                if (edtSearch.text.toString().trim().isEmpty()) return@click
                edtSearch.setText("")
                filterWithSearch(edtSearch.text.toString().trim())
                hideKeyboard()
            }

            edtSearch.onTextChange { string ->
                filterWithSearch(string.toString().trim())
            }
        }
    }

    private fun filterWithSearch(query: String) {
        binding.ivClear.isVisible = query.isNotEmpty()
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(dataList)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (item in dataList) {
                if (item.city.lowercase().contains(lowerCaseQuery) || item.region.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(item)
                }
            }
        }
        binding.lnEmpty.isVisible = filteredList.isEmpty()
        binding.rcvWorldClock.isVisible = filteredList.isNotEmpty()
        adapter.submitList(filteredList.toMutableList())
    }
}