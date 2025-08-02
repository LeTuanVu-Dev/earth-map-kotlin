package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.HomeModel
import com.earthmap.map.ltv.tracker.databinding.ItemHomeBinding

class MainAdapter(
    private val onClickItem: (HomeModel) -> Unit,
) : ListAdapter<HomeModel, MainAdapter.HomeViewHolder>(
    object : DiffUtil.ItemCallback<HomeModel>() {
        override fun areItemsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return oldItem.homeID == newItem.homeID
        }

        override fun areContentsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class HomeViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: HomeModel) {
            binding.ivIcon.setImageResource(item.icon)
            binding.tvTitle.text = binding.root.context.getString(item.title)
            binding.tvTitle.isSelected = true
            itemView.click {
                onClickItem(item)
            }
        }
    }
}
