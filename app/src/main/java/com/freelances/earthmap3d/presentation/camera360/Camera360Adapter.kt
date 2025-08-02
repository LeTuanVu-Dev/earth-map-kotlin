package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.camera360

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_IMAGE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Scale
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.loadImageThumb
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelCamera360
import com.earthmap.map.ltv.tracker.databinding.ItemCamera360Binding

class Camera360Adapter(
    private val onItemClick: (ModelCamera360) -> Unit
) : ListAdapter<ModelCamera360, Camera360Adapter.Camera360ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Camera360ViewHolder {
        val binding = ItemCamera360Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Camera360ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Camera360ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Camera360ViewHolder(private val binding: ItemCamera360Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelCamera360) {
            binding.tvTitle.text = item.name
            val image = API_DATA_IMAGE + item.image
            binding.imgCameraLive.loadImageThumb(image,item.id)
            binding.root.safeClick {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelCamera360>() {
        override fun areItemsTheSame(oldItem: ModelCamera360, newItem: ModelCamera360): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ModelCamera360, newItem: ModelCamera360): Boolean {
            return oldItem == newItem
        }
    }
}
