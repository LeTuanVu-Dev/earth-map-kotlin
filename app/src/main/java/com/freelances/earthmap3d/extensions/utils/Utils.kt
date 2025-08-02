package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationDataWrapper
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCameraDataWrapper
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlaceDataWrapper
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Utils {

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun getListDataSuggestNormal(context: Context): List<LocationModel> {
        val json = readJsonFromAssets(context, LOCATION_ASSET)
        val gson = Gson()

        val wrapper = gson.fromJson(json, LocationDataWrapper::class.java)
        val locations: List<LocationModel> = wrapper.data
        return locations
    }

    fun getListDataCamera360(context: Context): List<ModelCamera360> {
        val json = readJsonFromAssets(context, IMAGE_360_ASSET)
        val gson = Gson()

        val wrapper = gson.fromJson(json, ModelCameraDataWrapper::class.java)
        val locations: List<ModelCamera360> = wrapper.data
        return locations.sortedBy { it.name }
    }

    fun getListDataFamousPlace(context: Context): List<ModelFamousPlace> {
        val json = readJsonFromAssets(context, IMAGE_FAMOUS_ASSET)
        val gson = Gson()

        val wrapper = gson.fromJson(json, ModelFamousPlaceDataWrapper::class.java)
        val locations: List<ModelFamousPlace> = wrapper.data
        return locations
    }

    suspend fun getListDataSuggest(context: Context): List<LocationModel> =
        withContext(Dispatchers.IO) {
            val json = readJsonFromAssets(context, LOCATION_ASSET)
            val gson = Gson()
            gson.fromJson(json, LocationDataWrapper::class.java).data
        }

    fun getYoutubeId(url: String): String? {
        val videoId = Regex("v=([\\w-]{11})").find(url)?.groupValues?.get(1)
        return videoId
    }

    fun getYoutubeThumbnail(url: String): String? {
        val videoId = Regex("v=([\\w-]{11})").find(url)?.groupValues?.get(1)
        return videoId?.let { "https://img.youtube.com/vi/$it/0.jpg" }
    }

    fun loadOrCueVideo(
        youTubePlayer: YouTubePlayer,
        lifecycle: Lifecycle,
        videoId: String,
        startSeconds: Float
    ) {
        val canLoad = lifecycle.currentState == Lifecycle.State.RESUMED
        youTubePlayer.loadOrCueVideo(canLoad, videoId, startSeconds)
    }

    @JvmSynthetic
    internal fun YouTubePlayer.loadOrCueVideo(
        canLoad: Boolean,
        videoId: String,
        startSeconds: Float
    ) {
        if (canLoad) {
            loadVideo(videoId, startSeconds)
        } else {
            cueVideo(videoId, startSeconds)
        }
    }

    fun getRangeForZoomLevel(zoom: Int): Double {
        return when (zoom) {
            in 0..2 -> 20_000_000.0  // Cực xa, cả địa cầu
            in 3..5 -> 10_000_000.0  // Lục địa
            in 6..8 -> 2_500_000.0   // Quốc gia
            in 9..11 -> 800_000.0    // Thành phố
            in 12..14 -> 300_000.0   // Quận huyện
            in 15..17 -> 80_000.0    // Khu phố
            in 18..20 -> 20_000.0    // Sát mặt đất
            else -> 10_000.0
        }
    }

    fun setUpLoadingAnimation(view: View) {
        val rotationAnimation =
            ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotationAnimation.duration = 2000
        rotationAnimation.repeatCount = ObjectAnimator.INFINITE
        rotationAnimation.start()
    }

}