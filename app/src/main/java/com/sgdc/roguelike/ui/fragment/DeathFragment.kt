package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.MusicManager
import com.sgdc.roguelike.domain.save.GameProgress
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen
import kotlin.getValue

class DeathFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val gameViewModel: GameViewModel by activityViewModels()

    private lateinit var btnNext : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_death, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MusicManager.play("death_screen")

        GameProgress.setHighScore(requireContext(), gameViewModel.stageFloor.value ?: 0)

        btnNext = view.findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            MusicManager.stop("death_screen")
            MusicManager.play("main_menu")
            mainViewModel.navigateTo(Screen.Menu)
        }
    }
}