package com.sgdc.roguelike.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgdc.roguelike.R
import com.sgdc.roguelike.databinding.DialogStoreBinding
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.item.HealthPotion
import com.sgdc.roguelike.domain.item.ManaPotion
import com.sgdc.roguelike.ui.adapter.StoreAdapter
import com.sgdc.roguelike.ui.adapter.StoreItem
import com.sgdc.roguelike.ui.viewmodel.GameViewModel

class StoreDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private var _binding: DialogStoreBinding? = null
    private val binding get() = _binding!!
    private var isProcessingPurchase = false

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup the adapter once
        val storeAdapter = StoreAdapter(emptyList()) // Start with an empty list
        binding.rvStoreItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = storeAdapter
        }

        // --- ALL LOGIC IS NOW DRIVEN BY PLAYER DATA CHANGES ---
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            binding.tvGold.text = "Gold: ${player.money}"

            // Create a dynamic list of items for sale
            val itemsForSale = mutableListOf(
                StoreItem("Health Potion", 20, R.drawable.ic_health_potion) {
                    handlePurchase { gameViewModel.playerAddItem(HealthPotion()) }
                },
                StoreItem("Mana Potion", 30, R.drawable.ic_mana_potion) {
                    handlePurchase { gameViewModel.playerAddItem(ManaPotion()) }
                }
            )

            // Conditionally add the skill item OR show the message
            if (gameViewModel.isAllSkillsAvailable()) {
                itemsForSale.add(
                    StoreItem("Random Skill", 100, R.drawable.ic_random_skill) {
                        handleSkillPurchase()
                    }
                )
                binding.tvAllSkillsCollected.isVisible = false // Hide message
            } else {
                binding.tvAllSkillsCollected.isVisible = true // Show message
            }

            // Update the adapter with the final list of items
            storeAdapter.updateItems(itemsForSale)
        }

        binding.btnCloseStore.setOnClickListener {
            SfxManager.play("button")
            dismiss()
        }
    }
    private fun setupStoreItems() {
        val itemsForSale = listOf(
            StoreItem("Health Potion", 20, R.drawable.ic_health_potion) {
                handlePurchase { gameViewModel.playerAddItem(HealthPotion()) }
            },
            StoreItem("Mana Potion", 30, R.drawable.ic_mana_potion) {
                handlePurchase { gameViewModel.playerAddItem(ManaPotion()) }
            },
            StoreItem("Random Skill", 100, R.drawable.ic_random_skill) {
                handleSkillPurchase()
            }
        )

        binding.rvStoreItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = StoreAdapter(itemsForSale)
        }
    }

    private fun handlePurchase(purchaseAction: () -> Boolean) {
        if (isProcessingPurchase) return
        isProcessingPurchase = true

        val success = purchaseAction()
        if (!success) {
            SfxManager.play("decline")
            binding.tvWarning.visibility = View.VISIBLE
        } else {
            SfxManager.play("buy")
            binding.tvWarning.visibility = View.GONE
        }
        isProcessingPurchase = false
    }

    // --- MODIFIED THIS FUNCTION ---
    private fun handleSkillPurchase() {
        if (isProcessingPurchase) return
        isProcessingPurchase = true

        val purchasedSkill = gameViewModel.playerBuyRandomSkill()

        if (purchasedSkill == null) {
            SfxManager.play("decline")
            binding.tvWarning.text = "Cannot buy skill!"
            binding.tvWarning.visibility = View.VISIBLE
        } else {
            SfxManager.play("buy") // Or a more special sound like "gacha_success"
            binding.tvWarning.visibility = View.GONE

            // Show a Toast message to inform the player what they got
            val message = "You learned: ${purchasedSkill.name}!"
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        isProcessingPurchase = false
    }

    // --- REMOVED THE showGachaSkillDialog FUNCTION ---

    override fun onResume() {
        super.onResume()
        isProcessingPurchase = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}