package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.domain.bgm.MusicManager
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.Screen
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.save.GameProgress

class MenuFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var dungeonName : TextView
    private lateinit var highestScore : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dungeonName = view.findViewById(R.id.dungeonName)
        highestScore = view.findViewById(R.id.highestscoreText)

        val highestScoreSave : Int = GameProgress.getHighScore(requireContext())
        dungeonName.text = "Embark : Dark Forest"
        highestScore.text = getString(R.string.highest_floor, highestScoreSave.toString())


        view.findViewById<ImageButton>(R.id.newGameButton).setOnClickListener {
            SfxManager.play("play")
            MusicManager.stop("main_menu")
            mainViewModel.navigateTo(Screen.Rest)
            SfxManager.play("button")
        }

        view.findViewById<ImageButton>(R.id.settingImageButton).setOnClickListener {
            mainViewModel.navigateTo(Screen.Settings)
            SfxManager.play("button")
        }
    }
}