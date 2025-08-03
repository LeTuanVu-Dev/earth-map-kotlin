package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.traffic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.checkLocationPermission
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.hideKeyboard
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.onTextChange
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d.EarthMapLoader
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d.SearchSuggestAdapter
import com.earthmap.map.ltv.tracker.databinding.ActivityMapTrafficBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MapTrafficActivity : BaseActivity<ActivityMapTrafficBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMapTrafficBinding {
        return ActivityMapTrafficBinding.inflate(layoutInflater)
    }

    private val earthMapLoader: EarthMapLoader by inject()
    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val searchAdapter by lazy {
        SearchSuggestAdapter { model ->
            hideKeyboard()
            binding.edtSearch.setText(model.title)
            binding.edtSearch.clearFocus()
            moveToLocation(model.lat.toDouble(), model.lon.toDouble(), model.title)
            binding.rcvSuggest.gone()
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupMap()
        setupSearch()
        setupActions()
        observeViewModel()
    }

    private fun setupMap() {
        binding.mapTraffic.getFragment<SupportMapFragment>().getMapAsync { map ->
            Log.d("VuLT", "setupMap: $map")

            googleMap = map.apply {
                uiSettings.isZoomControlsEnabled = false
                uiSettings.isCompassEnabled = false
                uiSettings.isMyLocationButtonEnabled = false
                if (checkLocationPermission()) {
                    isMyLocationEnabled = false
                    isTrafficEnabled = true
                }
                binding.mapTraffic.post {
                    getCurrentLocation()
                }
            }
        }
    }

    private fun setupSearch() {
        binding.rcvSuggest.apply {
            adapter = searchAdapter
        }
    }

    private fun setupActions() {
        binding.apply {
            ivCoordinates.click { getCurrentLocation() }

            ivClear.click {
                edtSearch.text?.clear()
                rcvSuggest.gone()
                ivClear.gone()
            }

            ivBack.safeClick {
                finish()
            }
            edtSearch.onTextChange { string ->
                ivClear.isVisible = string?.isNotEmpty() == true
                binding.rcvSuggest.isVisible = string?.isNotEmpty() == true
                if (!string.isNullOrBlank()) {
                    earthMapLoader.searchNominatim(
                        this@MapTrafficActivity,
                        this@MapTrafficActivity.getString(R.string.traffic_map),
                        string.toString()
                    )
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            earthMapLoader.searchResults.collect { results ->
                searchAdapter.submitList(results)
            }
        }
    }

    private fun getCurrentLocation() {
        if (!checkLocationPermission()) {
            return
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                moveToLocation(
                    location.latitude,
                    location.longitude,
                    getString(R.string.app_name)
                )
            }
        }
    }

    private fun moveToLocation(lat: Double, lon: Double, title: String?) {
        val latLng = LatLng(lat, lon)
        currentMarker?.remove()
        currentMarker = googleMap?.addMarker(
            MarkerOptions().position(latLng).title(title)
        )
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
}