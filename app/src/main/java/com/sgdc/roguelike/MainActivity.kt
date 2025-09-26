package com.sgdc.roguelike

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash screen API
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.isReady.value == false
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Observe navigation
        viewModel.currentScreen.observe(this) { screen ->
            val fragment = when (screen) {
                Screen.Menu -> MenuFragment()
                Screen.Battle -> BattleFragment()
                Screen.Settings -> SettingsFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }

        // Simulate loading work (you can replace this with real init)
        window.decorView.postDelayed({
            viewModel.finishLoading()
        }, 1500)
    }
}
