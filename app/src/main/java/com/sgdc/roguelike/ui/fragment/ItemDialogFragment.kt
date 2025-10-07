package com.sgdc.roguelike.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgdc.roguelike.databinding.DialogItemBinding
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.ui.adapter.ItemAdapter
import com.sgdc.roguelike.ui.viewmodel.GameViewModel

class ItemDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private var _binding: DialogItemBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        // This tells the dialog's window to match the screen's width
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Create the adapter
        val itemAdapter = ItemAdapter { inventoryItem ->
            // This is the click handler for each item
            gameViewModel.playerUseItem(inventoryItem.item)
            SfxManager.play("button")
            dismiss()
        }

        // 2. Setup the RecyclerView
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemAdapter
        }

        // 3. Observe player data and update the adapter
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            // Handle empty state
            binding.tvNoItems.isVisible = player.items.isEmpty()
            binding.rvItems.isVisible = player.items.isNotEmpty()

            // Submit the list to the adapter for automatic updates
            itemAdapter.submitList(player.items)
        }

        binding.btnCloseItem.setOnClickListener {
            SfxManager.play("button")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}