package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WeatherUtils.getWeatherIconRes
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.HourlyForecast
import com.earthmap.map.ltv.tracker.databinding.ItemWeatherForHourBinding

class WeatherAdapter :
    ListAdapter<HourlyForecast, WeatherAdapter.WeatherHourViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHourViewHolder {
        val binding =
            ItemWeatherForHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherHourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherHourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WeatherHourViewHolder(private val binding: ItemWeatherForHourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HourlyForecast) {
            binding.apply {
                tvTime.text = item.time.takeLast(5).trim() // lấy giờ phút từ "2025-05-23 10:00"
                tvTemp.text = "${item.tempC}°C"
                imageView.setImageResource(getWeatherIconRes(item.conditionCode, item.isDay))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HourlyForecast>() {
        override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem == newItem
        }
    }
}
