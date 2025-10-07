package com.sgdc.roguelike.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sgdc.roguelike.R

class SkillDescriptionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_skills_description, container, false)

        val skillName = arguments?.getString("skillName") ?: "Unknown Skill"
        val manaCost = arguments?.getInt("manaCost") ?: 0
        val description = arguments?.getString("description") ?: "No description available."

        view.findViewById<TextView>(R.id.tvSkillName).text = skillName
        view.findViewById<TextView>(R.id.tvSkillManaCost).text = "Mana Cost: $manaCost"
        view.findViewById<TextView>(R.id.tvSkillDescription).text = description

        view.findViewById<View>(R.id.btnSkillOk).setOnClickListener { dismiss() }

        return view
    }

    companion object {
        fun newInstance(skillName: String, manaCost: Int, description: String): SkillDescriptionDialogFragment {
            val fragment = SkillDescriptionDialogFragment()
            val args = Bundle().apply {
                putString("skillName", skillName)
                putInt("manaCost", manaCost)
                putString("description", description)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
