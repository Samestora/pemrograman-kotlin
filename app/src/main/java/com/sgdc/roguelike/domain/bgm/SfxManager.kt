package com.sgdc.roguelike.domain.bgm

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.Sound
import com.sgdc.roguelike.domain.setting.SettingsManager

object SfxManager : Sound {
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()
    var muted : Boolean = false // <-- add this

    override fun init(context: Context) {
        muted = !SettingsManager.isSfxEnabled(context) // load saved state
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        soundMap["play"] = soundPool.load(context, R.raw.sfx_play_button, 1)
        soundMap["fireball"] = soundPool.load(context, R.raw.sfx_skill_fireball, 1)
        soundMap["slash"] = soundPool.load(context, R.raw.sfx_skill_slash, 1)
        soundMap["attack"] = soundPool.load(context, R.raw.sfx_basic_attack, 1)
    }

    override fun play(name: String?) {
        if (muted) return  // respect mute toggle
        name?.let {
            val soundId = soundMap[it]
            if (soundId != null) {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    override fun stop(name: String?) { /* Optional */ }

    override fun release() {
        soundPool.release()
    }

    override fun pause() {
        soundPool.autoPause()
    }

    override fun resume() {
        soundPool.autoResume()
    }
}