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

        binding.btnCloseStats.setOnClickListener { dismiss() }

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            binding.tvPlayerClass.text = player.name
            binding.tvPlayerHealth.text = "${player.health} / ${player.maxHealth}"
            binding.tvPlayerMana.text = "${player.mana} / ${player.maxMana}"
            binding.tvPlayerAttack.text = player.att.toString()
            binding.tvPlayerDefense.text = player.def.toString()
            binding.tvPlayerGold.text = player.money.toString()

            // Update skill section
            val skillContainer = binding.layoutPlayerSkills
            val noSkillText = binding.tvNoSkills
            skillContainer.removeAllViews()

            if (player.skills.isNotEmpty()) {
                noSkillText.visibility = View.GONE
                player.skills.forEach { skill ->
                    val tvSkill = TextView(requireContext()).apply {
                        text = "• ${skill.name}"
                        setTextColor(resources.getColor(android.R.color.white))
                        textSize = 16f
                        setPadding(0, 6, 0, 6)
                        setOnClickListener {
                            SkillDescriptionDialogFragment.newInstance(
                                skill.name,
                                skill.manaCost,
                                skill.description
                            ).show(parentFragmentManager, "SkillDescriptionDialog")
                        }
                    }
                    skillContainer.addView(tvSkill)
                }
            } else {
                noSkillText.visibility = View.VISIBLE
            }
        }
    }

    // Clean up the binding reference when the view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}