package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.camera360

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog.CoinDialog
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview.Camera360PreviewActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.purchase.PurchaseActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityCamera360Binding
import com.freelances.earthmap3d.presentation.camera360.Camera360Loader
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class Camera360Activity : BaseActivity<ActivityCamera360Binding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityCamera360Binding {
        return ActivityCamera360Binding.inflate(layoutInflater)
    }

    private val camera360Loader: Camera360Loader by inject()

    private var currentItem: ModelCamera360? = null
    private val camera360Adapter by lazy {
        Camera360Adapter { item ->
            currentItem = item
            showDialogUseCoin()
        }
    }

    private val coinDialog by lazy {
        CoinDialog().apply {
            setOnClick {
                if (preferenceHelper.coinNumber >= 5) {
                    postValueCoinAndMinusCoin()
                    currentItem?.let { openPreview(it) }
                } else {
                    navigateTo(PurchaseActivity::class.java)
                }
            }
        }
    }

    private fun showDialogUseCoin() {
        if (!coinDialog.isAdded) coinDialog.show(
            supportFragmentManager,
            Camera360Activity::class.java.name
        )
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        camera360Loader.loadImageCamera360FromAssets(this)
        initData()
        binding.ivBack.safeClick { finish() }
    }

    private fun initData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                camera360Loader.cameraResults.collect { list ->
                    if (list.isNotEmpty()) {
                        camera360Adapter.submitList(list)
                    }
                }
            }
        }
        binding.rcvCameraImage.adapter = camera360Adapter
    }

    private fun openPreview(item: ModelCamera360) {
        navigateTo(
            Camera360PreviewActivity::class.java, bundleOf(
                ARG_DATA to item
            )
        )
    }
}