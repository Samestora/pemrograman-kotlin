package com.sgdc.roguelike.ui.fragment

import android.annotation.SuppressLint
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
import com.sgdc.roguelike.domain.setting.SettingsManager

class SettingsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    // i use switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val musicToggle = view.findViewById<Switch>(R.id.musicToggle)
        val sfxToggle = view.findViewById<Switch>(R.id.sfxToggle)

        // Load saved states
        musicToggle.isChecked = SettingsManager.isMusicEnabled(requireContext())
        sfxToggle.isChecked = SettingsManager.isSfxEnabled(requireContext())


        // Music toggle
        musicToggle.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setMusicEnabled(requireContext(), isChecked)
            MusicManager.muted = !isChecked
            if (isChecked) {
                MusicManager.unMute()
            } else {
                MusicManager.mute()
            }
        }

        // SFX toggle
        sfxToggle.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setSfxEnabled(requireContext(), isChecked)
            SfxManager.muted = !isChecked
        }

        view.findViewById<Button>(R.id.backToMenuButton)?.setOnClickListener {
            viewModel.navigateTo(Screen.Menu)
        }


        // Example: back button goes back to menu
        view.findViewById<Button>(R.id.backToMenuButton)?.setOnClickListener {
            viewModel.navigateTo(Screen.Menu)
        }
    }
}