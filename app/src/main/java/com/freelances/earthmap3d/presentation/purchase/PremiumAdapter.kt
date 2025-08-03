package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ItemPurchaseBinding
import com.freelances.earthmap3d.presentation.purchase.AppPurchase

class PremiumAdapter(
    private val onItemClick: (ProductDetails) -> Unit
) : ListAdapter<ProductDetails, PremiumAdapter.FamousViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamousViewHolder {
        val binding =
            ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FamousViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamousViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class FamousViewHolder(private val binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductDetails, position: Int) {
            binding.tvCoin.text = getCoinWithPosition(position)
            binding.tvCost.text = AppPurchase.getInstance(binding.tvCost.context).getPrice(item)
            binding.tvApply.safeClick {
                onItemClick(item)
            }
        }
    }

    private fun getCoinWithPosition(position: Int): String {
        return when (position) {
            0 -> "50 coins"
            1 -> "100 coins"
            2 -> "200 coins"
            3 -> "300 coins"
            4 -> "500 coins"
            else -> "1000 coins"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductDetails>() {
        override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem == newItem
        }
    }
}
