package com.freelances.earthmap3d.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import com.freelances.earthmap3d.base.BaseActivity
import com.freelances.earthmap3d.com.freelances.earthmap3d.models.HomeModel
import com.freelances.earthmap3d.com.freelances.earthmap3d.presentation.main.MainAdapter
import com.freelances.earthmap3d.databinding.ActivityMainBinding
import com.freelances.earthmap3d.extensions.utils.AIR_QUALITY
import com.freelances.earthmap3d.extensions.utils.CAMERA_360
import com.freelances.earthmap3d.extensions.utils.CAMERA_LIVE
import com.freelances.earthmap3d.extensions.utils.COMPASS
import com.freelances.earthmap3d.extensions.utils.EARTH_3D
import com.freelances.earthmap3d.extensions.utils.FAMOUS_PLACE
import com.freelances.earthmap3d.extensions.utils.TRAFFIC_MAP
import com.freelances.earthmap3d.extensions.utils.WORLD_CLOCK

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