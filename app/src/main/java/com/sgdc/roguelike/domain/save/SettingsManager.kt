package com.sgdc.roguelike.domain.save

import android.content.Context
import androidx.core.content.edit

object SettingsManager {
    private const val PREFS_NAME = "game_settings"
    private const val KEY_MUSIC = "music_enabled"
    private const val KEY_SFX = "sfx_enabled"

    fun isMusicEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_MUSIC, true) // default = true
    }

    fun setMusicEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_MUSIC, enabled) }
    }

    fun isSfxEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SFX, true) // default = true
    }

    fun setSfxEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_SFX, enabled) }
    }
}
