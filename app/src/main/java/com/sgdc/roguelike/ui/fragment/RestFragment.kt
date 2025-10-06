package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.Screen
import androidx.core.view.isGone
import com.sgdc.roguelike.domain.bgm.SfxManager

class RestFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var itemsContainer : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show player stats after rest
        val restStatBonus = view.findViewById<TextView>(R.id.restHeal)
        val restSkillGain = view.findViewById<TextView>(R.id.restSkillGain)
        itemsContainer = view.findViewById(R.id.itemContainer)

        val newSkill = gameViewModel.grantRandomSkill()

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            restStatBonus?.text = getString(R.string.rest_heal_message, player.health, player.maxHealth)
            if (newSkill != null) {
                restSkillGain?.text = getString(R.string.skill_gain_message, newSkill.name)
            } else {
                restSkillGain?.text = getString(R.string.all_skill_acquired_message)
            }
        }

        // Handle "Next Stage"
        view.findViewById<ImageButton>(R.id.nextStageButton)?.setOnClickListener {
            gameViewModel.resetBattle()
            mainViewModel.navigateTo(Screen.Battle)
        }

        view.findViewById<Button>(R.id.storeBtn)?.setOnClickListener {
            StoreDialogFragment().show(parentFragmentManager, "StoreDialog")
            SfxManager.play("button")
        }

        view.findViewById<Button>(R.id.itemBtn)?.setOnClickListener {
            ItemDialogFragment().show(parentFragmentManager, "ItemDialog")
            SfxManager.play("button")
        }
    }

    private fun populateItems() {
        itemsContainer.removeAllViews()
        val player = gameViewModel.player.value ?: return

        for (inventoryItem in player.items) {
            val btn = Button(requireContext()).apply {
                text = "${inventoryItem.item.name} x${inventoryItem.amount}"
                setOnClickListener {
                    gameViewModel.playerUseItem(inventoryItem.item)
                    itemsContainer.visibility = View.GONE
                    SfxManager.play("button")
                }
            }
            itemsContainer.addView(btn)
        }
    }
}