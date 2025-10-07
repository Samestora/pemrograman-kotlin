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
import com.sgdc.roguelike.ui.viewmodel.GameViewModel

class MenuFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val gameViewModel: GameViewModel by activityViewModels()

    private lateinit var tvEmbarkDungeon: TextView
    private lateinit var tvHighscore: TextView
    private lateinit var tvContinue: TextView
    private lateinit var btnNewGame: ImageButton
    private lateinit var btnContinue: ImageButton
    private lateinit var btnSettings: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEmbarkDungeon = view.findViewById(R.id.tvEmbarkDungeon)
        tvHighscore = view.findViewById(R.id.tvHighscore)
        btnNewGame = view.findViewById(R.id.btnNewGame)
        btnContinue = view.findViewById(R.id.btnContinue)
        btnSettings = view.findViewById(R.id.btnSettings)
        tvContinue = view.findViewById(R.id.tvContinue)

        tvEmbarkDungeon.text = "Embark: Dark Forest"
        tvHighscore.text = "Highscore : Dark Forest ${GameProgress.getHighScore(requireContext())}"

        view.findViewById<ImageButton>(R.id.btnNewGame).setOnClickListener {
            SfxManager.play("play")
            MusicManager.stop("main_menu")
            mainViewModel.navigateTo(Screen.Rest)
            gameViewModel.resetGame()
            SfxManager.play("button")
        }

        view.findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            mainViewModel.navigateTo(Screen.Settings)
            SfxManager.play("button")
        }
    }
}