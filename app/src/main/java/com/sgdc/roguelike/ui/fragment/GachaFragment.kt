package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class GachaFragment : Fragment() {

    private lateinit var tvGachaResult: TextView
    private lateinit var btnOk: Button
    private val statList = listOf("attack", "maxHealth", "maxMana", "defence")

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        fun newInstance() = GachaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_gacha, container, false)
        tvGachaResult = view.findViewById(R.id.tvGachaResult)
        btnOk = view.findViewById(R.id.btnOk)

        btnOk.visibility = View.INVISIBLE

        btnOk.setOnClickListener {
            mainViewModel.navigateTo(Screen.Rest)
        }

        startGachaAnimation()

        return view
    }

    private fun startGachaAnimation() {
        scope.launch {
            val startTime = System.currentTimeMillis()
            var chosenStat: String
            var increaseAmount: Int

            while (System.currentTimeMillis() - startTime < 3000) {
                chosenStat = statList.random()
                increaseAmount = (1..5).random()

                tvGachaResult.text =
                    "Rolling...\nStat: $chosenStat +$increaseAmount"

                delay(100) // update tiap 0.1 detik
            }

            // hasil final
            val finalStat = statList.random()
            val finalAmount = (1..5).random()
            gameViewModel.gachaStat(finalStat, finalAmount)

            tvGachaResult.text =
                "🎉 Final Result!\nStat: $finalStat +$finalAmount"
            btnOk.visibility = View.VISIBLE
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}