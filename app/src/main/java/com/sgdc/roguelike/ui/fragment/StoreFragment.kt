package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.item.HealthPotion
import com.sgdc.roguelike.domain.item.ManaPotion
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class StoreFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_store, container, false)
        val btnBuyHealthPotion = view.findViewById<Button>(R.id.btnBuyHealthPotion)
        val backBtn = view.findViewById<Button>(R.id.backBtn)
        val btnBuyManaPotion = view.findViewById<Button>(R.id.btnBuyManaPotion)
        val warningText = view.findViewById<TextView>(R.id.tvWarning)
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            view.findViewById<TextView>(R.id.tvCurrency).text = player.money.toString()
        }

        // Buy Health Potion
        btnBuyHealthPotion.setOnClickListener {
            val buy = gameViewModel.playerAddItem(HealthPotion())
            if(!buy){
                warningText.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1500)
                    warningText.visibility = View.GONE
                }
            }
            else{ SfxManager.play("buy") }
        }

        // Buy Mana Potion
        btnBuyManaPotion.setOnClickListener {
            val buy = gameViewModel.playerAddItem(ManaPotion())
            if(!buy){
                warningText.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1500)
                    warningText.visibility = View.GONE
                }
            }
            else{ SfxManager.play("buy") }
        }

        backBtn.setOnClickListener {
            mainViewModel.navigateTo(Screen.Rest)
            warningText.visibility = View.GONE
            SfxManager.play("button")
        }

        return view
    }
}
