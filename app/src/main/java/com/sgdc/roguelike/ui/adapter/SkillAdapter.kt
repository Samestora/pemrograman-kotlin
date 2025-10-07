package com.sgdc.roguelike.ui.adapter // Or your appropriate package

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.skill.Skill

class SkillAdapter(
    private val skills: List<Skill>,
    private val onSkillClicked: (Skill) -> Unit
) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    // 1. UPDATE THE VIEWHOLDER
    // It now finds the TextView from the item's root view.
    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val skillNameTextView: TextView = itemView.findViewById(R.id.tvSkillName)
    }

    // 2. UPDATE ONCREATEVIEWHOLDER
    // It no longer casts the layout to a TextView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        // Inflate the layout to a generic View (which is the CardView)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_skill, parent, false)
        return SkillViewHolder(view)
    }

    // 3. UPDATE ONBINDVIEWHOLDER
    // It now uses the correct reference to the TextView.
    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill = skills[position]
        // Use the TextView reference from inside the ViewHolder
        holder.skillNameTextView.text = "• ${skill.name}"
        holder.itemView.setOnClickListener {
            onSkillClicked(skill)
        }
    }

    override fun getItemCount() = skills.size
}