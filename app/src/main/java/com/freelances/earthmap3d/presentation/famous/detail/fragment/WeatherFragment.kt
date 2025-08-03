package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WeatherUtils.getWeatherCurrentState
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WeatherUtils.mapForecastToDisplay
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.WeatherResponse
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.FamousDetailViewModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter.WeatherAdapter
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter.WeatherDayAdapter
import com.earthmap.map.ltv.tracker.databinding.FragmentWeatherFamousBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WeatherFragment : BaseFragment<FragmentWeatherFamousBinding>() {
    companion object {
        fun newInstance(): WeatherFragment {
            val fragment = WeatherFragment()
            return fragment
        }
    }


    override fun inflateBinding(inflater: LayoutInflater): FragmentWeatherFamousBinding {
        return FragmentWeatherFamousBinding.inflate(layoutInflater)
    }

    private val famousDetailViewModel: FamousDetailViewModel by activityViewModels()

    private val weatherHourAdapter by lazy {
        WeatherAdapter()
    }

    private val weatherDayAdapter by lazy {
        WeatherDayAdapter()
    }

    override fun updateUI(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            combine(
                famousDetailViewModel.itemCurrent,
                famousDetailViewModel.cityIsoCode
            ) { item, cityCode ->
                Pair(item, cityCode)
            }.collect { (item, cityCode) ->
                if (item != null && !cityCode.isNullOrEmpty()) {
                    famousDetailViewModel.fetchWeather(cityCode, item.latitude, item.longitude)
                }
            }
        }

        lifecycleScope.launch {
            famousDetailViewModel.weatherData.collect { weather ->
                if (weather == null) return@collect
                setUpData(weather)
            }
        }

    }

    private fun setUpData(weather : WeatherResponse){
        binding.apply {
            tvSub.text = getString(getWeatherCurrentState(weather.data.current.conditionCode))
            tvTemperature.text = "${weather.data.current.tempC}°C"
            val sunriseTime = weather.data.forecast.firstOrNull()?.day?.sunrise
            val sunsetTime = weather.data.forecast.firstOrNull()?.day?.sunset

            tvTimeDay.text = sunriseTime
            tvTimeNight.text = sunsetTime
            tvWind.text = weather.data.current.windMps.toString()

            rcvHourDay.adapter = weatherHourAdapter
            weatherHourAdapter.submitList(weather.data.forecast.firstOrNull()?.hour)

            rcvTimeWeek.adapter = weatherDayAdapter
            val displayList = mapForecastToDisplay(weather.data.forecast, weather.data.time)
            weatherDayAdapter.submitList(displayList)

        }
    }
}