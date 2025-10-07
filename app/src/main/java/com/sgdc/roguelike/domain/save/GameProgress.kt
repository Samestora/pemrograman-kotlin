package com.sgdc.roguelike.domain.save

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sgdc.roguelike.domain.character.Monster
import com.sgdc.roguelike.domain.character.Player

object GameProgress {
    private const val PREFS_NAME = "game_progress"
    private const val KEY_HIGHSCORE = "highscore"

    fun getHighScore(context: Context): Int {
        val prefs = context.getSharedPreferences(GameProgress.PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_HIGHSCORE, 0) // default 0
    }

    fun setHighScore(context: Context, value: Int) {
        val prefs = context.getSharedPreferences(GameProgress.PREFS_NAME, Context.MODE_PRIVATE)
        if (getHighScore(context) < value) {
            prefs.edit { putInt(KEY_HIGHSCORE, value) }
        }
    }

}