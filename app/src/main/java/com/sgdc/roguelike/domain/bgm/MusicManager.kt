package com.sgdc.roguelike.domain.bgm

import android.content.Context
import android.media.MediaPlayer
import com.sgdc.roguelike.App
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.Sound
import com.sgdc.roguelike.domain.setting.SettingsManager

object MusicManager : Sound {
    private var mediaPlayer: MediaPlayer? = null
    private val musicMap = mutableMapOf<String, Int>()
    private var currentMusic: String? = null
    var muted : Boolean = false // <-- add this

    override fun init(context: Context) {
        muted = !SettingsManager.isMusicEnabled(context)
        musicMap["main_menu"] = R.raw.main_menu
    }

    override fun play(name: String?) {
        if (muted || name == null) return  // respect mute toggle

        if (currentMusic == name && mediaPlayer?.isPlaying == true) return

        stop()
        val resId = musicMap[name] ?: return
        mediaPlayer = MediaPlayer.create(App.Companion.context, resId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        currentMusic = name
    }

    override fun stop(name: String?) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentMusic = null
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun resume() {
        mediaPlayer?.start()
    }
}