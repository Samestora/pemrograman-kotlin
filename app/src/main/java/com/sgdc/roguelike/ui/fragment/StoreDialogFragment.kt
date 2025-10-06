package com.sgdc.roguelike.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.item.HealthPotion
import com.sgdc.roguelike.domain.item.ManaPotion
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import kotlin.getValue

class StoreDialogFragment : DialogFragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvGold = view.findViewById<TextView>(R.id.tvGold)
        val tvWarning = view.findViewById<TextView>(R.id.tvWarning)
        val btnBuyHealth = view.findViewById<Button>(R.id.btnBuyHealthPotion)
        val btnBuyMana = view.findViewById<Button>(R.id.btnBuyManaPotion)
        val btnClose = view.findViewById<Button>(R.id.btnCloseStore)
        val btnBuyRandomSkill = view.findViewById<Button>(R.id.btnBuyRandomSkill)
        val textBuyRandomSkill = view.findViewById<TextView>(R.id.tvBuyRandomSkill)

        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            tvGold.text = "Gold: ${player.money}"
            if (gameViewModel.isAllSkillsAvailable()) {
                btnBuyRandomSkill.visibility = View.VISIBLE
            } else {
                btnBuyRandomSkill.visibility = View.GONE
                textBuyRandomSkill.text = getString(R.string.all_skill_acquired_message)
            }
        }

        btnBuyHealth.setOnClickListener {
            val success = gameViewModel.playerAddItem(HealthPotion())
            if (!success) {
                SfxManager.play("decline")
                tvWarning.visibility = View.VISIBLE
            } else {
                SfxManager.play("buy")
                tvWarning.visibility = View.GONE
            }
        }

        btnBuyMana.setOnClickListener {
            val success = gameViewModel.playerAddItem(ManaPotion())
            if (!success) {
                SfxManager.play("decline")
                tvWarning.visibility = View.VISIBLE
            } else {
                SfxManager.play("buy")
                tvWarning.visibility = View.GONE
            }
        }

        btnBuyRandomSkill.setOnClickListener {
            val success = gameViewModel.playerBuyRandomSkill()
            if (!success) {
                SfxManager.play("decline")
                tvWarning.visibility = View.VISIBLE
            }else {
                SfxManager.play("buy")
                tvWarning.visibility = View.GONE
            }
        }

        btnClose.setOnClickListener {
            SfxManager.play("button")
            dismiss()
        }
    }
}


