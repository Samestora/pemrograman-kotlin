package com.sgdc.roguelike.domain.bgm

import android.content.Context
import android.media.MediaPlayer
import com.sgdc.roguelike.App
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.save.SettingsManager

object MusicManager : Sound {
    private var mediaPlayer: MediaPlayer? = null
    private val musicMap = mutableMapOf<String, Int>()
    private var currentMusic: String? = null
    var muted : Boolean = false // <-- add this

    override fun init(context: Context) {
        muted = !SettingsManager.isMusicEnabled(context)
        musicMap["main_menu"] = R.raw.main_menu
        musicMap["death_screen"] = R.raw.death_screen_ochiba
        musicMap["rest"] = R.raw.rest
        musicMap["battle"] = R.raw.battle
        musicMap["boss"] = R.raw.boss
    }

    override fun play(name: String?) {
        if (name == null) return

        if (currentMusic == name && mediaPlayer?.isPlaying == true) {
            applyVolume()
            return
        }

        stop()
        val resId = musicMap[name] ?: return
        mediaPlayer = MediaPlayer.create(App.Companion.context, resId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        applyVolume()
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


    override fun mute() {
        muted = true
        applyVolume()
    }

    override fun unMute() {
        muted = false
        applyVolume()
    }

    // It can only be applied here, since apply volume in sfx needs soundId
    private fun applyVolume() {
        val vol = if (muted) 0f else 1f
        mediaPlayer?.setVolume(vol, vol)
    }
}