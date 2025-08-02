package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.cameraLive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Utils.getYoutubeThumbnail
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.loadImage
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LocationModel
import com.earthmap.map.ltv.tracker.databinding.ItemCameraLiveBinding

class CameraLiveAdapter(
    private val onItemClick: (LocationModel) -> Unit
) : ListAdapter<LocationModel, CameraLiveAdapter.SearchViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemCameraLiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val binding: ItemCameraLiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationModel) {
            val thumb = getYoutubeThumbnail(item.videoLink)
            binding.ivImage.loadImage(thumb?:item.videoLink,item.id)
            binding.tvTitle.text = item.name
            binding.tvTitle.isSelected = true
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem == newItem
        }
    }
}
