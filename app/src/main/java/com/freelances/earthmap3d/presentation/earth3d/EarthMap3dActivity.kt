package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.TEMPLATE_FORMAT
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.URL_TEMPLATE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getRangeForZoomLevel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.hideKeyboard
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.onTextChange
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ActivityEarthMap3dBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import earth.worldwind.WorldWindow
import earth.worldwind.geom.AltitudeMode
import earth.worldwind.geom.Angle
import earth.worldwind.geom.LookAt
import earth.worldwind.geom.Position
import earth.worldwind.globe.elevation.coverage.BasicElevationCoverage
import earth.worldwind.layer.BackgroundLayer
import earth.worldwind.layer.RenderableLayer
import earth.worldwind.layer.atmosphere.AtmosphereLayer
import earth.worldwind.layer.mercator.WebMercatorLayerFactory
import earth.worldwind.layer.starfield.StarFieldLayer
import earth.worldwind.render.image.ImageSource
import earth.worldwind.shape.Placemark
import earth.worldwind.shape.PlacemarkAttributes
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class EarthMap3dActivity : BaseActivity<ActivityEarthMap3dBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityEarthMap3dBinding {
        return ActivityEarthMap3dBinding.inflate(layoutInflater)
    }

    private val earthMapLoader: EarthMapLoader by inject()
    private lateinit var wwd: WorldWindow
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationLayer = RenderableLayer("Location")

    private val searchAdapter by lazy {
        SearchSuggestAdapter { model ->
            hideKeyboard()
            binding.edtSearch.setText(model.title)
            binding.edtSearch.clearFocus()
            moveToLocation(model.lat.toDouble(), model.lon.toDouble())
            binding.rcvSuggest.gone()
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        setupSearch()
        actionClick()
        observeViewModel()
    }

    private fun actionClick() {
        binding.apply {
            ivCoordinates.safeClick {
                getCurrentLocation()
            }

            ivClear.click {
                if (edtSearch.text.toString().trim().isEmpty()) return@click
                edtSearch.setText("")
                ivClear.isVisible = false
            }

            ivBack.safeClick {
                finish()
            }

            edtSearch.onTextChange { string ->
                ivClear.isVisible = string?.isNotEmpty() == true
                binding.rcvSuggest.isVisible = string?.isNotEmpty() == true
                if (!string.isNullOrBlank()) {
                    earthMapLoader.searchNominatim(
                        this@EarthMap3dActivity,
                        this@EarthMap3dActivity.getString(R.string.earth_3d),
                        string.toString()
                    )
                }
            }
        }
    }

    private fun setupSearch() {
        binding.rcvSuggest.apply {
            adapter = searchAdapter
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                moveToLocation(it.latitude, it.longitude)
            }
        }
    }

    private fun initData() {
        wwd = WorldWindow(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        wwd.engine.layers.addLayer(locationLayer)
        getCurrentLocation()
        wwd.engine.layers.apply {
            addLayer(BackgroundLayer())
            addLayer(
                WebMercatorLayerFactory.createLayer(
                    urlTemplate = URL_TEMPLATE,
                    imageFormat = TEMPLATE_FORMAT,
                    name = this@EarthMap3dActivity.getString(R.string.earth_3d)
                )
            )
            addLayer(StarFieldLayer())
            addLayer(AtmosphereLayer())
        }
        wwd.engine.globe.elevationModel.addCoverage(BasicElevationCoverage())
        binding.earthGlobal.addView(wwd)

    }

    private fun moveToLocation(lat: Double, lon: Double) {
        val pos = Position.fromDegrees(lat, lon, 0.0)
        val lookAt = LookAt().apply {
            set(
                Angle.fromDegrees(lat),
                Angle.fromDegrees(lon),
                0.0,
                AltitudeMode.ABSOLUTE,
                getRangeForZoomLevel(3),
                Angle.fromDegrees(0.0),
                Angle.fromDegrees(0.0),
                Angle.fromDegrees(0.0)
            )
        }
        wwd.engine.cameraFromLookAt(lookAt)
        addLocationMarker(pos)
    }

    private fun addLocationMarker(position: Position) {
        locationLayer.clearRenderables()

        val placemark = Placemark(position).apply {
            altitudeMode = AltitudeMode.CLAMP_TO_GROUND
            isEyeDistanceScaling = false
            attributes = PlacemarkAttributes.createWithImage(
                ImageSource.fromResource(R.drawable.ic_location_item_search)
            )
        }

        locationLayer.addRenderable(placemark)
        wwd.requestRedraw()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            earthMapLoader.searchResults.collect { results ->
                searchAdapter.submitList(results)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        wwd.onResume()
    }

    override fun onPause() {
        super.onPause()
        wwd.onPause()

    }
}