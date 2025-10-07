package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.MusicManager
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.save.GameProgress
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class GachaFragment : Fragment() {

    private lateinit var tvGachaResult: TextView
    private lateinit var tvGachaTitle: TextView
    private lateinit var nextStageButton: ImageButton
    private lateinit var btnOk: FrameLayout

    private val statList = listOf("Attack", "Max Health", "Max Mana", "Defense")

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance() = GachaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_gacha, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GameProgress.setHighScore(requireContext(), gameViewModel.stageFloor.value ?: 0)

        tvGachaResult = view.findViewById(R.id.tvGachaResult)
        tvGachaTitle = view.findViewById(R.id.tvGachaTitle)
        nextStageButton = view.findViewById(R.id.nextStageButton)
        btnOk = view.findViewById(R.id.btnOk)

        btnOk.visibility = View.INVISIBLE
        nextStageButton.setOnClickListener {
            SfxManager.play("button")
            MusicManager.play("rest")
            mainViewModel.navigateTo(Screen.Rest)
        }

        startGachaAnimation()
    }

    private fun startGachaAnimation() {
        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()
            var chosenStat: String
            var increaseAmount: Int

            while (System.currentTimeMillis() - startTime < 3000) {
                chosenStat = statList.random()
                increaseAmount = (1..5).random()

                tvGachaTitle.text = getString(R.string.stat_gacha_title_rolling)
                tvGachaResult.text = getString(R.string.stat_gacha_gain_rolling, chosenStat, increaseAmount)

                delay(100)
            }

            // Final result
            val finalStat = statList.random()
            val finalAmount = (1..5).random()

            val player = gameViewModel.player.value ?: return@launch
            val oldValue = when (finalStat) {
                "Attack" -> player.att
                "Max Health" -> player.maxHealth
                "Max Mana" -> player.maxMana
                "Defense" -> player.def
                else -> 0
            }

            // Apply the gacha bonus
            gameViewModel.gachaStat(finalStat, finalAmount)

            val newPlayer = gameViewModel.player.value ?: return@launch
            val newValue = when (finalStat) {
                "Attack" -> newPlayer.att
                "Max Health" -> newPlayer.maxHealth
                "Max Mana" -> newPlayer.maxMana
                "Defense" -> newPlayer.def
                else -> 0
            }

            tvGachaTitle.text = getString(R.string.stat_gacha_title)
            tvGachaResult.text = getString(R.string.stat_gacha_gain, finalStat, oldValue, newValue, finalAmount)
            btnOk.visibility = View.VISIBLE
        }
    }

}