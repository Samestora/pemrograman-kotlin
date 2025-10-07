package com.sgdc.roguelike.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgdc.roguelike.R
import com.sgdc.roguelike.databinding.ItemStoreBinding

// Data class to represent an item in the store
data class StoreItem(
    val name: String,
    val price: Int,
    val iconResId: Int,
    val onBuy: () -> Unit // A function to execute when "Buy" is clicked
)

class StoreAdapter(
    private var storeItems: List<StoreItem>
) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(storeItems[position])
    }

    override fun getItemCount() = storeItems.size

    fun updateItems(newItems: List<StoreItem>) {
        storeItems = newItems
        notifyDataSetChanged() // Refresh the list
    }

    class StoreViewHolder(private val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storeItem: StoreItem) {
            binding.ivItemIcon.setImageResource(storeItem.iconResId)
            binding.tvItemName.text = storeItem.name
            binding.tvItemPrice.text = "${storeItem.price} Gold"
            binding.btnBuyItem.setOnClickListener {
                storeItem.onBuy()
            }
        }
    }

}