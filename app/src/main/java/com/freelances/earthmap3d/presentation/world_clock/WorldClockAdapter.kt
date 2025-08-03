package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.world_clock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.databinding.ItemWorldClockBinding
import com.freelances.earthmap3d.models.WorldClockItem

class WorldClockAdapter :
    ListAdapter<WorldClockItem, WorldClockAdapter.WorldClockViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldClockViewHolder {
        val binding =
            ItemWorldClockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorldClockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorldClockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorldClockViewHolder(private val binding: ItemWorldClockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WorldClockItem) {
            binding.apply {
                tvTitle.text = item.city
                tvSub.text = item.region
                tvTime.text = item.time
                tvGmtOffset.text = item.gmtOffset
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WorldClockItem>() {
        override fun areItemsTheSame(oldItem: WorldClockItem, newItem: WorldClockItem): Boolean {
            return oldItem.city == newItem.city
        }

        override fun areContentsTheSame(oldItem: WorldClockItem, newItem: WorldClockItem): Boolean {
            return oldItem == newItem
        }
    }
}

