package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.AirUtils
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirData
import com.earthmap.map.ltv.tracker.databinding.ItemAirQualityBinding

class AirQualityAdapter(
    private val onItemClick: (AirData) -> Unit
) : ListAdapter<AirData, AirQualityAdapter.AirQualityViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirQualityViewHolder {
        val binding =
            ItemAirQualityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AirQualityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AirQualityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AirQualityViewHolder(private val binding: ItemAirQualityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AirData) {
            binding.apply {
                val cityName = item.city.name
                val parts = cityName.split(",").map { it.trim() }
                val city = parts.getOrNull(0) ?: cityName
                val country = parts.getOrNull(1) ?: city

                tvTitle.text = city
                tvSub.text = country
                tvQuality.text = if (item.aqi >= 0) item.aqi.toString() else "N/A"

                if (item.aqi >= 0) {
                    val colorText = ContextCompat.getColor(binding.root.context, AirUtils.getColorSetForAqi(item.aqi).textColorRes)
                    val colorBg = ContextCompat.getColor(binding.root.context, AirUtils.getColorSetForAqi(item.aqi).backgroundColorRes)
                    tvQuality.backgroundTintList = ColorStateList.valueOf(colorBg)
                    tvQuality.setTextColor(colorText)
                }

                root.safeClick {
                    onItemClick(item)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AirData>() {
        override fun areItemsTheSame(oldItem: AirData, newItem: AirData): Boolean {
            return oldItem.idx == newItem.idx
        }

        override fun areContentsTheSame(oldItem: AirData, newItem: AirData): Boolean {
            return oldItem == newItem
        }
    }
}
