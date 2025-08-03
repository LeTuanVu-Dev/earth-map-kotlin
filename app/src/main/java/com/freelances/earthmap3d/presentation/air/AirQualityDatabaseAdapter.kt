package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.ModelAirQualityDatabase
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.AirUtils
import com.earthmap.map.ltv.tracker.databinding.ItemAirQualityBinding

class AirQualityDatabaseAdapter : ListAdapter<ModelAirQualityDatabase, AirQualityDatabaseAdapter.AirQualityViewHolder>(
    DiffCallback()
) {

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

        fun bind(item: ModelAirQualityDatabase) {
            binding.apply {
                val cityName = item.nameFull
                val city = item.title
                tvTitle.text = city
                tvSub.text = cityName
                tvQuality.text = if (item.airQuality >= 0) item.airQuality.toString() else "N/A"

                if (item.airQuality >= 0) {
                    val colorText = ContextCompat.getColor(binding.root.context, AirUtils.getColorSetForAqi(item.airQuality).textColorRes)
                    val colorBg = ContextCompat.getColor(binding.root.context, AirUtils.getColorSetForAqi(item.airQuality).backgroundColorRes)
                    tvQuality.backgroundTintList = ColorStateList.valueOf(colorBg)
                    tvQuality.setTextColor(colorText)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelAirQualityDatabase>() {
        override fun areItemsTheSame(oldItem: ModelAirQualityDatabase, newItem: ModelAirQualityDatabase): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ModelAirQualityDatabase, newItem: ModelAirQualityDatabase): Boolean {
            return oldItem == newItem
        }
    }
}
