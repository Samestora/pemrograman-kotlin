package com.sgdc.roguelike.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.GameViewModel

class PlayerStatsDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_player_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvPlayerClass)
        val tvHealth = view.findViewById<TextView>(R.id.tvPlayerHealth)
        val tvMana = view.findViewById<TextView>(R.id.tvPlayerMana)
        val tvAttack = view.findViewById<TextView>(R.id.tvPlayerAttack)
        val tvDefense = view.findViewById<TextView>(R.id.tvPlayerDefense)
        val tvGold = view.findViewById<TextView>(R.id.tvPlayerGold)

        val closeBtn = view.findViewById<Button>(R.id.btnCloseStats)
        closeBtn.setOnClickListener { dismiss() }

        // Observe player data from ViewModel
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            tvName.text = "Name: ${player.name}"
            tvHealth.text = "Health: ${player.health} / ${player.maxHealth}"
            tvMana.text = "Mana: ${player.mana} / ${player.maxMana}"
            tvAttack.text = "Attack: ${player.att}"
            tvDefense.text = "Defense: ${player.def}"
            tvGold.text = "Gold: ${player.money}"
        }
    }
}