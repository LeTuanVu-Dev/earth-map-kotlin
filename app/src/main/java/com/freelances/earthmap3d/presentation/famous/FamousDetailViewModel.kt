package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.WeatherResponse
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.CityCodeFetcher
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.WeatherFetcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FamousDetailViewModel(
    private val fetcher: CityCodeFetcher,
    private val weatherFetcher: WeatherFetcher
) : ViewModel() {

    private val _cityIsoCode = MutableStateFlow<String?>("")
    val cityIsoCode: StateFlow<String?> get() = _cityIsoCode


    private val _itemCurrent = MutableStateFlow<ModelFamousPlace?>(null)
    val itemCurrent: StateFlow<ModelFamousPlace?> get() = _itemCurrent


    fun setItemCurrent(item: ModelFamousPlace) {
        _itemCurrent.value = item
    }

    fun fetchIsoCode(latitude: Double, longitude: Double, cityName: String) {
        viewModelScope.launch {
            val isoCode = fetcher.getCityIsoCode(latitude, longitude, cityName)
            _cityIsoCode.value = isoCode
        }
    }

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?>  get() = _weatherData

    fun fetchWeather(
        cityCode: String,
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch {
            val response = weatherFetcher.getWeather(
                cityCode = cityCode,
                latitude = lat,
                longitude = long
            )
            _weatherData.value = response
        }
    }

}
