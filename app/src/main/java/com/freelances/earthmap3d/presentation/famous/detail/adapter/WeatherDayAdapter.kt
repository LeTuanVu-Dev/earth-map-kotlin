package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.WeatherUtils.getWeatherType
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ForecastDisplayItem
import com.earthmap.map.ltv.tracker.databinding.ItemWeatherForDayBinding

class WeatherDayAdapter :
    ListAdapter<ForecastDisplayItem, WeatherDayAdapter.WeatherDayViewHolder>(DiffCallback()) {

    inner class WeatherDayViewHolder(private val binding: ItemWeatherForDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastDisplayItem) {
            binding.apply {
                tvTitle.text = item.dayOfWeek
                tvDate.text = item.dateDisplay
                tvTempMin.text = item.minTemp
                tvTempMax.text = item.maxTemp
                imageView.setImageResource(getWeatherType(item.conditionCode))
                if (item == currentList[currentList.size - 1]){
                    vLine.gone()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder {
        val binding =
            ItemWeatherForDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ForecastDisplayItem>() {
        override fun areItemsTheSame(
            oldItem: ForecastDisplayItem,
            newItem: ForecastDisplayItem
        ): Boolean {
            return oldItem.dayOfWeek == newItem.dayOfWeek
        }

        override fun areContentsTheSame(
            oldItem: ForecastDisplayItem,
            newItem: ForecastDisplayItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}