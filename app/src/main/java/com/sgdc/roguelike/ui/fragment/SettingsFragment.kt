package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.domain.bgm.MusicManager
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.Screen
import com.sgdc.roguelike.domain.bgm.SfxManager

class SettingsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Switch>(R.id.musicToggle)?.setOnCheckedChangeListener { _, isChecked ->
            MusicManager.muted = !isChecked
            if (MusicManager.muted) {
                MusicManager.stop()
            } else {
                MusicManager.play("main_menu") // or current screen’s music
            }
        }

        view.findViewById<Switch>(R.id.sfxToggle)?.setOnCheckedChangeListener { _, isChecked ->
            SfxManager.muted = !isChecked
        }


        // Example: back button goes back to menu
        view.findViewById<Button>(R.id.backToMenuButton)?.setOnClickListener {
            viewModel.navigateTo(Screen.Menu)
        }
    }
}