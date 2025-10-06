package com.sgdc.roguelike.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.item.HealthPotion
import com.sgdc.roguelike.domain.item.ManaPotion

class ItemDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemContainer = view.findViewById<LinearLayout>(R.id.itemContainer)
        val closeBtn = view.findViewById<Button>(R.id.btnCloseItem)

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            itemContainer.removeAllViews()

            player.items.filter { it.item is ManaPotion }.forEach { inventoryItem ->
                val btn = Button(requireContext()).apply {
                    text = "${inventoryItem.item.name} x${inventoryItem.amount}"
                    setOnClickListener {
                        gameViewModel.playerUseItem(inventoryItem.item)
                        SfxManager.play("button")
                        dismiss()
                    }
                }
                itemContainer.addView(btn)
            }
            player.items.filter { it.item is HealthPotion }.forEach { inventoryItem ->
                val btn = Button(requireContext()).apply {
                    text = "${inventoryItem.item.name} x${inventoryItem.amount}"
                    setOnClickListener {
                        gameViewModel.playerUseItem(inventoryItem.item)
                        SfxManager.play("button")
                        dismiss()
                    }
                }
                itemContainer.addView(btn)
            }
        }

        closeBtn.setOnClickListener {
            SfxManager.play("button")
            dismiss()
        }
    }
}

