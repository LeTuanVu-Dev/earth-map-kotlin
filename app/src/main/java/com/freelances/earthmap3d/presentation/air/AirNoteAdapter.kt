package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.AirNote
import com.earthmap.map.ltv.tracker.databinding.ItemAirNoteBinding

class AirNoteAdapter : ListAdapter<AirNote, AirNoteAdapter.AirQualityViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirQualityViewHolder {
        val binding =
            ItemAirNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AirQualityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AirQualityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AirQualityViewHolder(private val binding: ItemAirNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AirNote) {
            binding.apply {
                imgItem.setImageResource(item.icon)
                tvNumber.text = binding.root.context.getString(item.value)
                tvState.text = binding.root.context.getString(item.state)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AirNote>() {
        override fun areItemsTheSame(oldItem: AirNote, newItem: AirNote): Boolean {
            return oldItem.icon == newItem.icon
        }

        override fun areContentsTheSame(oldItem: AirNote, newItem: AirNote): Boolean {
            return oldItem == newItem
        }
    }
}
