package com.sgdc.roguelike.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sgdc.roguelike.ui.fragment.DeathFragment
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.domain.bgm.MusicManager
import com.sgdc.roguelike.R
import com.sgdc.roguelike.ui.viewmodel.Screen
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.ui.fragment.BattleFragment
import com.sgdc.roguelike.ui.fragment.GachaFragment
import com.sgdc.roguelike.ui.fragment.MenuFragment
import com.sgdc.roguelike.ui.fragment.RestFragment
import com.sgdc.roguelike.ui.fragment.SettingsFragment
import com.sgdc.roguelike.ui.fragment.StoreFragment

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        MusicManager.init(this)
        SfxManager.init(this)

        // Splash screen API
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.isReady.value == false
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Observe navigation
        viewModel.currentScreen.observe(this) { screen ->
            val fragment = when (screen) {
                Screen.Menu -> MenuFragment()
                Screen.Battle -> BattleFragment()
                Screen.Rest -> RestFragment()
                Screen.Death -> DeathFragment()
                Screen.Settings -> SettingsFragment()
                Screen.Gacha -> GachaFragment()
                Screen.Store -> StoreFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }

        // Simulate loading work (you can replace this with real init)
        window.decorView.postDelayed({
            viewModel.finishLoading()
            MusicManager.play("main_menu")
        }, 4000)
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pause()
        SfxManager.pause()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resume()
        SfxManager.resume()
    }
}