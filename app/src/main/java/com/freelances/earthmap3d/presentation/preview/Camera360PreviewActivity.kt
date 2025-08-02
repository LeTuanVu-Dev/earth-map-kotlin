package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_IMAGE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.loadImageWithConvertUrlForBitmap
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.parcelable
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.databinding.ActivityCamera360PreviewBinding
import com.panoramagl.PLImage
import com.panoramagl.PLManager
import com.panoramagl.PLSphericalPanorama

class Camera360PreviewActivity : BaseActivity<ActivityCamera360PreviewBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCamera360PreviewBinding {
        return ActivityCamera360PreviewBinding.inflate(layoutInflater)
    }

    private val itemPreview by lazy {
        runCatching {
            intent.extras?.parcelable<ModelCamera360>(ARG_DATA)
        }.getOrNull()
    }

    private lateinit var plManager: PLManager
    private lateinit var panorama: PLSphericalPanorama

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        initAction()
    }

    private fun initAction() {
        binding.ivBack.safeClick {
            finish()
        }
    }

    private fun initData() {
        plManager = PLManager(this)
        plManager.setContentView(binding.root)
        plManager.onCreate()

        binding.tvTitle.text = itemPreview?.name ?: getString(R.string.camera_360)
        panorama = PLSphericalPanorama()
        panorama.camera.lookAt(10.0f, 110.0f)
        val imageUrl = API_DATA_IMAGE + itemPreview?.image
        loadImageWithConvertUrlForBitmap(this@Camera360PreviewActivity, imageUrl) { resource ->
            panorama.setImage(PLImage(resource, false))
            plManager.panorama = panorama
        }
    }

    override fun onResume() {
        super.onResume()
        plManager.onResume()
    }

    override fun onPause() {
        plManager.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        plManager.onDestroy()
        super.onDestroy()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return plManager.onTouchEvent(event)
    }
}