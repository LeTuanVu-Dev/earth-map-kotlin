package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_IMAGE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.loadImage
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelTravels
import com.earthmap.map.ltv.tracker.databinding.ItemImageFamousBinding

class InfoAdapter : ListAdapter<ModelTravels, InfoAdapter.ImageSliderViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding =
            ItemImageFamousBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageSliderViewHolder(private val binding: ItemImageFamousBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelTravels) {
            val image = API_DATA_IMAGE + item.imageUrl
            binding.imageView.loadImage(image, item.id)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelTravels>() {
        override fun areItemsTheSame(oldItem: ModelTravels, newItem: ModelTravels): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ModelTravels, newItem: ModelTravels): Boolean {
            return oldItem == newItem
        }
    }
}