package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.preview

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getYoutubeId
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.parcelable
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive.CameraLiveAdapter
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive.CameraLiveLoader
import com.earthmap.map.ltv.tracker.databinding.ActivityLivePreviewBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CameraLivePreviewActivity : BaseActivity<ActivityLivePreviewBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLivePreviewBinding {
        return ActivityLivePreviewBinding.inflate(layoutInflater)
    }

    private val cameraLiveLoader: CameraLiveLoader by inject()

    private val itemData by lazy {
        runCatching {
            intent.extras?.parcelable<LocationModel>(ARG_DATA)
        }.getOrNull()
    }
    private var dataList = mutableListOf<LocationModel>()
    private var currentLive: LocationModel? = null
    private var filteredList = arrayListOf<LocationModel>()

    private val cameraLiveAdapter by lazy {
        CameraLiveAdapter { item ->
            playNewVideo(item)
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        currentLive = itemData
        itemData?.let { initYouTubePlayerView(it) }
        setUpData()
        binding.ivBack.safeClick {
            finish()
        }
    }

    private fun setUpData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cameraLiveLoader.cameraResults.collect { list ->
                    if (list.isNotEmpty()) {
                        dataList.clear()
                        dataList.addAll(list)
                        cameraLiveAdapter.submitList(dataList.filter { it.id != itemData?.id }
                            .shuffled().take(10))
                    }
                }
            }
        }
        binding.rcvLive.adapter = cameraLiveAdapter
    }

    private var youTubePlayerRef: YouTubePlayer? = null

    private fun initYouTubePlayerView(itemData: LocationModel) {
        lifecycle.addObserver(binding.ytPreview)

        binding.ytPreview.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayerRef = youTubePlayer // Lưu lại để dùng sau

                getYoutubeId(itemData.videoLink)?.let { videoId ->
                    youTubePlayer.loadOrCueVideo(lifecycle,videoId, 1f)
                }
            }
        })
    }

    private fun playNewVideo(item: LocationModel) {
        val videoId = getYoutubeId(item.videoLink)
        if (videoId != null && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            youTubePlayerRef?.loadOrCueVideo(lifecycle, videoId, 0f)
        }

        filteredList.clear()
        filteredList.addAll(dataList.filter { it.id != item.id }.shuffled().take(10))
        cameraLiveAdapter.submitList(filteredList)
        binding.rcvLive.scrollToPosition(0)
    }
}