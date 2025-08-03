package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.air

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.gone
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelAirQualitySearch
import com.earthmap.map.ltv.tracker.databinding.ItemSearchBinding

class SearchAirQualityAdapter(
    private val onItemClick: (ModelAirQualitySearch) -> Unit
) : ListAdapter<ModelAirQualitySearch, SearchAirQualityAdapter.SearchViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelAirQualitySearch) {
            binding.tvName.text = item.title
            binding.ivRow.gone()
            binding.tvSub.text = item.nameFull
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ModelAirQualitySearch>() {
        override fun areItemsTheSame(oldItem: ModelAirQualitySearch, newItem: ModelAirQualitySearch): Boolean {
            return oldItem.geo == newItem.geo
        }

        override fun areContentsTheSame(oldItem: ModelAirQualitySearch, newItem: ModelAirQualitySearch): Boolean {
            return oldItem == newItem
        }
    }
}
