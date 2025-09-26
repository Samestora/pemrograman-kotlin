package com.sgdc.roguelike;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MenuFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.playGameButton).setOnClickListener {
            viewModel.navigateTo(Screen.Battle)
        }

        view.findViewById<ImageButton>(R.id.settingImageButton).setOnClickListener {
            viewModel.navigateTo(Screen.Settings)
        }

        view.findViewById<ImageButton>(R.id.exitImageButton).setOnClickListener {
            requireActivity().finish()
        }
    }
}

