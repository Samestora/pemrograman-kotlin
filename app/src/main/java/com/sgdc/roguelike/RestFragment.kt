package com.sgdc.roguelike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class RestFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show player stats after rest
        val restStatBonus = view.findViewById<TextView>(R.id.restStatBonus)
        val restSkillGain = view.findViewById<TextView>(R.id.restSkillGain)
        val newSkill = gameViewModel.grantRandomSkill()

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            restStatBonus.text = "You gain +20 HP \n (HP: ${player.health}/${player.maxHealth})"
            restSkillGain.text = "You gained ${newSkill.name}!"
        }

        // Handle "Next Stage"
        view.findViewById<ImageButton>(R.id.nextStageButton).setOnClickListener {
            // Heal player when rest
            gameViewModel.playerRest()
            gameViewModel.resetBattle()
            mainViewModel.navigateTo(Screen.Battle)
        }
    }
}

