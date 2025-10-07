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
import com.sgdc.roguelike.domain.save.GameProgress

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
        gameViewModel.playerRest()

        val restStatBonus = view.findViewById<TextView>(R.id.restHeal)
        val currentFloor = view.findViewById<TextView>(R.id.currentFloor)

        // Observe player stats and update heal message
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            restStatBonus.text = "Gain 20 HP & 10 MP"
        }

        gameViewModel.stageFloor.observe(viewLifecycleOwner) { stageFloor ->
            currentFloor.text = "Floor $stageFloor"
        }


        view.findViewById<ImageButton>(R.id.btnInspect)?.setOnClickListener {
            PlayerStatsDialogFragment().show(parentFragmentManager, "PlayerStatsDialog")
            SfxManager.play("button")
        }

        // Visit Store
        view.findViewById<ImageButton>(R.id.btnStore)?.setOnClickListener {
            StoreDialogFragment().show(parentFragmentManager, "StoreDialog")
            SfxManager.play("button")
        }

        // Backpack (use potion dialog)
        view.findViewById<ImageButton>(R.id.btnBackpack)?.setOnClickListener {
            ItemDialogFragment().show(parentFragmentManager, "ItemDialog")
            SfxManager.play("button")
        }

        // Continue (next battle)
        view.findViewById<ImageButton>(R.id.btnContinue)?.setOnClickListener {
            gameViewModel.resetBattle()
            mainViewModel.navigateTo(Screen.Battle)
            SfxManager.play("button")
        }
    }
}
