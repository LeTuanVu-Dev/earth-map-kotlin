package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.CAMERA_360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.HomeModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.setting.SettingActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityMainBinding
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.AIR_QUALITY
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.CAMERA_LIVE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.COMPASS
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.EARTH_3D
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.FAMOUS_PLACE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.TRAFFIC_MAP
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WORLD_CLOCK

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val homeAdapter by lazy {
        MainAdapter {
            clickItemHome(it)
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            ivSetting.safeClick {
                navigateTo(SettingActivity::class.java)
            }
        }
    }

    private fun initData() {
        binding.rcvItemHome.adapter = homeAdapter
        homeAdapter.submitList(HomeModel.listHome)
    }


    private fun clickItemHome(item: HomeModel) {
        when (item.homeID) {
            CAMERA_360 -> {}

            CAMERA_LIVE -> {}

            EARTH_3D -> {}

            FAMOUS_PLACE -> {}

            TRAFFIC_MAP -> {}

            WORLD_CLOCK -> {}

            AIR_QUALITY -> {}

            COMPASS -> {}

            else -> Unit
        }
    }

}