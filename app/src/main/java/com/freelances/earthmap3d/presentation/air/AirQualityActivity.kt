package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.ModelAirQualityDatabase
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.AirUtils
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.hideKeyboard
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.onTextChange
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ActivityAirQualityBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AirQualityActivity : BaseActivity<ActivityAirQualityBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityAirQualityBinding {
        return ActivityAirQualityBinding.inflate(layoutInflater)
    }

    private val airQualityViewModel: AirQualityViewModel by viewModel()
    private val listDb: MutableList<ModelAirQualityDatabase> = mutableListOf()

    private val airQualityAdapter by lazy {
        AirQualityAdapter { item ->
            val name = item.city.name
            val nameShort = name.split(",").firstOrNull()?.trim() ?: name
            val modelAirQualityDatabase = ModelAirQualityDatabase(
                title = nameShort,
                nameFull = name,
                geo = "${item.city.geo.firstOrNull()};${item.city.geo.lastOrNull()}",
                airQuality = item.aqi
            )
            binding.rcvAirQuality.adapter = airQualityDatabaseAdapter
            airQualityViewModel.insert(modelAirQualityDatabase)
            binding.ivClear.performClick()
        }
    }

    private val searchAdapter by lazy {
        SearchAirQualityAdapter { item ->
            hideKeyboard()
            val name = item.nameFull
            val nameShort = item.title
            val modelAirQualityDatabase =
                ModelAirQualityDatabase(title = nameShort, nameFull = name, geo = item.geo)
            binding.rcvAirQuality.adapter = airQualityDatabaseAdapter
            airQualityViewModel.insert(modelAirQualityDatabase)
            binding.ivClear.performClick()
        }
    }
    private val airNoteAdapter by lazy {
        AirNoteAdapter()
    }
    private val airQualityDatabaseAdapter by lazy {
        AirQualityDatabaseAdapter()
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        setUpData()
        actionClick()
    }

    private fun actionClick() {
        binding.apply {

            ivBack.safeClick {
                finish()
            }
            ivReload.safeClick {
                hideKeyboard()
                airQualityViewModel.syncDbFromApiIfHasData()
            }

            ivClear.click {
                if (edtSearch.text.toString().trim().isEmpty()) return@click
                edtSearch.setText("")
                binding.rcvSuggest.gone()
            }

            edtSearch.onTextChange { string ->
                ivClear.isVisible = string?.isNotEmpty() == true
                binding.rcvSuggest.isVisible = string?.isNotEmpty() == true
                if (!string.isNullOrBlank()) {
                    airQualityViewModel.searchCity(
                        this@AirQualityActivity.getString(R.string.air_quality),
                        string.toString()
                    )
                }
            }
        }
    }


    private fun setUpData() {
        binding.rcvNote.adapter = airNoteAdapter
        binding.rcvSuggest.adapter = searchAdapter
        airNoteAdapter.submitList(AirUtils.listNoteAir)

        airQualityViewModel.checkHasDataDbWithQuery()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    airQualityViewModel.cityAqiList.collect { list ->
                        if (list.isNotEmpty()) {
                            binding.tvReComment.text = getString(R.string.recommend)
                            binding.rcvSuggest.gone()
                            airQualityAdapter.submitList(list.toList())
                            binding.frLoading.gone()
                        }
                        if (!binding.frLoading.isVisible) {
                            binding.lnEmpty.isVisible = list.isEmpty()
                        }
                    }
                }

                launch {
                    airQualityViewModel.isLoading.collect { loading ->
                        binding.frLoading.isVisible = loading || (listDb.isEmpty() && airQualityAdapter.currentList.isEmpty())
                    }
                }

                launch {
                    airQualityViewModel.searchResults.collect { results ->
                        searchAdapter.submitList(results)
                        val isTyping = binding.rcvSuggest.isGone
                        val hasResults = results.isNotEmpty()
                        binding.lnEmpty.isVisible = !hasResults && !isTyping
                    }
                }

                launch {
                    airQualityViewModel.items.collect { results ->
                        if (results.isNotEmpty()) {
                            listDb.clear()
                            listDb.addAll(results)
                            binding.rcvSuggest.gone()
                            airQualityDatabaseAdapter.submitList(results)
                            binding.tvReComment.text = getString(R.string.recent)
                            binding.frLoading.gone()
                        }
                    }
                }

                launch {
                    airQualityViewModel.hasData.collect { hasData ->
                        binding.rcvAirQuality.adapter =
                            if (hasData) airQualityDatabaseAdapter else airQualityAdapter
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val isTyping = binding.rcvSuggest.isGone
        binding.lnEmpty.isVisible = !isTyping
    }

}
