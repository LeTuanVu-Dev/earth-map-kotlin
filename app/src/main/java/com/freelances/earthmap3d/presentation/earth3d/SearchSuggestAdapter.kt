package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.earth3d

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelSearchGoogleMap
import com.earthmap.map.ltv.tracker.databinding.ItemSearchBinding

class SearchSuggestAdapter(
    private val onItemClick: (ModelSearchGoogleMap) -> Unit
) : ListAdapter<ModelSearchGoogleMap, SearchSuggestAdapter.SearchViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelSearchGoogleMap) {
            binding.tvName.text = item.title
            binding.tvSub.text = item.nameFull
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelSearchGoogleMap>() {
        override fun areItemsTheSame(oldItem: ModelSearchGoogleMap, newItem: ModelSearchGoogleMap): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
        }

        override fun areContentsTheSame(oldItem: ModelSearchGoogleMap, newItem: ModelSearchGoogleMap): Boolean {
            return oldItem == newItem
        }
    }
}
