package com.sgdc.roguelike

import android.content.Context
import android.media.MediaPlayer

object MusicManager : Sound {
    private var mediaPlayer: MediaPlayer? = null
    private val musicMap = mutableMapOf<String, Int>() // name → resId
    private var currentMusic: String? = null

    override fun init(context: Context) {
        musicMap["main_menu"] = R.raw.main_menu
    }

    override fun play(name: String?) {
        if (name == null) return

        if (currentMusic == name && mediaPlayer?.isPlaying == true) return

        stop()

        val resId = musicMap[name] ?: return
        mediaPlayer = MediaPlayer.create(App.context, resId)
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
