package com.sgdc.roguelike

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
}
