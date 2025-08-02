package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.AIR_QUALITY
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.CAMERA_360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.CAMERA_LIVE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.COMPASS
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.EARTH_3D
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.FAMOUS_PLACE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.TRAFFIC_MAP
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WORLD_CLOCK
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.HomeModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.camera360.Camera360Activity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive.CameraLiveActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.compass.CompassActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog.ExitAppDialog
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview.CameraLivePreviewActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.setting.SettingActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val homeAdapter by lazy {
        MainAdapter {
            clickItemHome(it)
        }
    }

    private val exitDialog by lazy {
        ExitAppDialog(this) {
            finishAffinity()
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        preferenceHelper.isFinishFirstFlow = true
        initData()
        initListeners()
    }

    private fun showExitDialog() {
        if (!exitDialog.isShowing) {
            exitDialog.show()
        }
    }

    private fun initListeners() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })

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
            CAMERA_360 -> {
                navigateTo(Camera360Activity::class.java)
            }

            CAMERA_LIVE -> {
                navigateTo(CameraLiveActivity::class.java)
            }

            EARTH_3D -> {}

            FAMOUS_PLACE -> {}

            TRAFFIC_MAP -> {}

            WORLD_CLOCK -> {}

            AIR_QUALITY -> {}

            COMPASS -> {
                navigateTo(CompassActivity::class.java)
            }

            else -> Unit
        }
    }

}