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
import com.sgdc.roguelike.databinding.DialogPlayerStatsBinding

class PlayerStatsDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    // This is the key property for View Binding in Fragments
    private var _binding: DialogPlayerStatsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    // The code to fix the shrinking dialog
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate and return the binding root
        _binding = DialogPlayerStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access views directly from the binding object - no more findViewById!
        binding.btnCloseStats.setOnClickListener { dismiss() }

        // Observe player data from ViewModel
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            binding.tvPlayerClass.text = player.name
            binding.tvPlayerHealth.text = "${player.health} / ${player.maxHealth}"
            binding.tvPlayerMana.text = "${player.mana} / ${player.maxMana}"
            binding.tvPlayerAttack.text = player.att.toString() // Ensure values are strings
            binding.tvPlayerDefense.text = player.def.toString()
            binding.tvPlayerGold.text = player.money.toString()
        }
    }

    // Clean up the binding reference when the view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}