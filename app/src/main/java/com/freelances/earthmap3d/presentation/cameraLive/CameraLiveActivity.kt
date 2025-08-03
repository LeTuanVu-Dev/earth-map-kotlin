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
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.camera360.Camera360Activity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog.CoinDialog
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview.CameraLivePreviewActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.purchase.PurchaseActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityCameraLiveBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CameraLiveActivity : BaseActivity<ActivityCameraLiveBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCameraLiveBinding {
        return ActivityCameraLiveBinding.inflate(layoutInflater)
    }
    private var currentItem: LocationModel? = null

    private val cameraLiveAdapter by lazy {
        CameraLiveAdapter { item ->
            currentItem = item
            showDialogUseCoin()

        }
    }
    private val cameraLiveLoader: CameraLiveLoader by inject()

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
                        navigateForResult(
                            CameraLivePreviewActivity::class.java,
                            bundleOf(
                                ARG_DATA to it
                            )
                        )
                    }
                } else {
                    navigateTo(PurchaseActivity::class.java)
                }
            }
        }
    }

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