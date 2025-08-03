package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.API_DATA_IMAGE
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.loadImageThumb
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.databinding.ItemCamera360Binding

class FamousAdapter(
    private val onItemClick: (ModelFamousPlace) -> Unit
) : ListAdapter<ModelFamousPlace, FamousAdapter.FamousViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamousViewHolder {
        val binding = ItemCamera360Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FamousViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamousViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FamousViewHolder(private val binding: ItemCamera360Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelFamousPlace) {
            binding.tvTitle.text = item.name
            val image = API_DATA_IMAGE + item.travels[0].imageUrl
            binding.imgCameraLive.loadImageThumb(image,item.id)
            binding.root.safeClick {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelFamousPlace>() {
        override fun areItemsTheSame(oldItem: ModelFamousPlace, newItem: ModelFamousPlace): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ModelFamousPlace, newItem: ModelFamousPlace): Boolean {
            return oldItem == newItem
        }
    }
}
