package com.sgdc.roguelike

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class RestFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Heal the player when they rest
        gameViewModel.playerHeal(20) // you can decide amount

        // Show player stats after rest
        val bonusText = view.findViewById<TextView>(R.id.restStatBonus)
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            bonusText.text = "You gain +20 HP (HP: ${player.health}/${player.maxHealth})"
        }

        // Handle "Next Stage"
        view.findViewById<ImageButton>(R.id.nextStageButton).setOnClickListener {
            gameViewModel.resetBattle()
            mainViewModel.navigateTo(Screen.Battle)
        }
    }
}

