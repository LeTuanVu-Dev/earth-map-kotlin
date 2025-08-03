package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.FamousDetailViewModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter.InfoAdapter
import com.earthmap.map.ltv.tracker.databinding.FragmentTravelFamousBinding
import kotlinx.coroutines.launch

class TravelFragment : BaseFragment<FragmentTravelFamousBinding>() {
    companion object {
        fun newInstance(): TravelFragment {
            val fragment = TravelFragment()
            return fragment
        }
    }


    override fun inflateBinding(inflater: LayoutInflater): FragmentTravelFamousBinding {
        return FragmentTravelFamousBinding.inflate(layoutInflater)
    }

    private val famousDetailViewModel: FamousDetailViewModel by activityViewModels()
    private val imageSliderAdapter by lazy {
        InfoAdapter()
    }

    override fun updateUI(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            famousDetailViewModel.itemCurrent.collect { item ->
                if (item != null) {
                    setUpData(item)
                }
            }
        }
    }

    private fun setUpData(item: ModelFamousPlace) {
        binding.apply {
            tvTitle.text = item.name
            indicatorView.setIndicatorMaxCount(item.travels.size)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setActiveIndicator()
                }
            })
            viewPager.adapter = imageSliderAdapter
            val imageList = item.travels
            imageSliderAdapter.submitList(imageList)
        }
    }

    private fun getCurrentItem(): Int {
        return binding.viewPager.currentItem
    }

    private fun setActiveIndicator() {
        val indexActive = getCurrentItem()
        binding.indicatorView.setIndicatorActive(indexActive)
    }
}