package com.sgdc.roguelike.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgdc.roguelike.databinding.DialogPlayerStatsBinding
import com.sgdc.roguelike.ui.adapter.SkillAdapter // <-- Import the new adapter
import com.sgdc.roguelike.ui.viewmodel.GameViewModel

class PlayerStatsDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    // View Binding property
    private var _binding: DialogPlayerStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        // Fix for the dialog shrinking
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
        _binding = DialogPlayerStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseStats.setOnClickListener { dismiss() }

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            // This part remains the same
            binding.tvPlayerClass.text = player.name
            binding.tvPlayerHealth.text = "HP: ${player.health} / ${player.maxHealth}"
            binding.tvPlayerMana.text = "MP: ${player.mana} / ${player.maxMana}"
            binding.tvPlayerAttack.text = player.att.toString()
            binding.tvPlayerDefense.text = player.def.toString()
            binding.tvPlayerGold.text = player.money.toString()

            // --- REFACTORED SKILL SECTION using RecyclerView ---
            if (player.skills.isNotEmpty()) {
                // Show the list and hide the "no skills" text
                binding.tvNoSkills.visibility = View.GONE
                binding.rvPlayerSkills.visibility = View.VISIBLE

                // 1. Create the adapter with the player's skills and the click handler
                val skillAdapter = SkillAdapter(player.skills) { skill ->
                    // This lambda function is executed when a skill is clicked
                    SkillDescriptionDialogFragment.newInstance(
                        skill.name,
                        skill.manaCost,
                        skill.description
                    ).show(parentFragmentManager, "SkillDescriptionDialog")
                }

                // 2. Set the adapter on the RecyclerView
                binding.rvPlayerSkills.adapter = skillAdapter

                // 3. Set a LayoutManager (required for RecyclerView to position items)
                binding.rvPlayerSkills.layoutManager = LinearLayoutManager(requireContext())

            } else {
                // If there are no skills, hide the list and show the "no skills" text
                binding.tvNoSkills.visibility = View.VISIBLE
                binding.rvPlayerSkills.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding reference to avoid memory leaks
        _binding = null
    }
}