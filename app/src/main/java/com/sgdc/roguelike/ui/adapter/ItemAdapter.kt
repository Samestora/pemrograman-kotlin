package com.sgdc.roguelike.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgdc.roguelike.R
import com.sgdc.roguelike.databinding.ItemInventoryBinding
import com.sgdc.roguelike.domain.item.HealthPotion
import com.sgdc.roguelike.domain.item.InventoryItem
import com.sgdc.roguelike.domain.item.ManaPotion

class ItemAdapter(
    private val onItemClicked: (InventoryItem) -> Unit
) : ListAdapter<InventoryItem, ItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class ItemViewHolder(private val binding: ItemInventoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(inventoryItem: InventoryItem, onItemClicked: (InventoryItem) -> Unit) {
            // Set the text for the two separate TextViews
            binding.tvItemName.text = inventoryItem.item.name
            binding.tvItemQuantity.text = "Quantity: ${inventoryItem.amount}"

            // ✨ Pro Tip: Set a unique icon for each item type
            val iconRes = when (inventoryItem.item) {
                is HealthPotion -> R.drawable.ic_health_potion // Replace with your drawable
                is ManaPotion -> R.drawable.ic_mana_potion // Replace with your drawable
                else -> R.drawable.ic_default_item   // A default icon
            }
            binding.ivItemIcon.setImageResource(iconRes)

            binding.root.setOnClickListener {
                onItemClicked(inventoryItem)
            }
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<InventoryItem>() {
    override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        // Checks if the items are the same entity (e.g., by a unique ID or name)
        return oldItem.item.name == newItem.item.name
    }

    override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        // Checks if the item's data has changed (e.g., quantity)
        return oldItem == newItem
    }
}