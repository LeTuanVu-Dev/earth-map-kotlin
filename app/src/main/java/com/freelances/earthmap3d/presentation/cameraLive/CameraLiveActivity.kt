package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview.CameraLivePreviewActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityCameraLiveBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CameraLiveActivity : BaseActivity<ActivityCameraLiveBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCameraLiveBinding {
        return ActivityCameraLiveBinding.inflate(layoutInflater)
    }

    private val cameraLiveAdapter by lazy {
        CameraLiveAdapter { item ->
            navigateForResult(
                CameraLivePreviewActivity::class.java,
                bundleOf(
                    ARG_DATA to item
                )
            )
        }
    }
    private val cameraLiveLoader: CameraLiveLoader by inject()

    override fun updateUI(savedInstanceState: Bundle?) {
        cameraLiveLoader.loadDataLiveFromAssets(this)
        initData()

        binding.ivBack.safeClick {
            finish()
        }

    }

    private fun initData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cameraLiveLoader.cameraResults.collect { list ->
                    if (list.isNotEmpty()) {
                        cameraLiveAdapter.submitList(list)
                    }
                }
            }
        }
        binding.rcvCameraImage.adapter = cameraLiveAdapter
    }
}