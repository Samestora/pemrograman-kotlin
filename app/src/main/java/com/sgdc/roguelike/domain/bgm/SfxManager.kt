package com.sgdc.roguelike.domain.bgm

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.save.SettingsManager

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
        soundMap["Fireball"] = soundPool.load(context, R.raw.sfx_skill_fireball, 1)
        soundMap["Slash"] = soundPool.load(context, R.raw.sfx_skill_slash, 1)
        soundMap["Flame Burst"] = soundPool.load(context, R.raw.sfx_skill_flame_burst, 1)
        soundMap["Heal"] = soundPool.load(context, R.raw.sfx_skill_heal, 1)
        soundMap["Ice Spike"] = soundPool.load(context, R.raw.sfx_skill_ice_spike, 1)
        soundMap["Thrust"] = soundPool.load(context, R.raw.sfx_skill_thrust, 1)
        soundMap["Thunder Strike"] = soundPool.load(context, R.raw.sfx_skill_thunder_strike, 1)
        soundMap["Bite"] = soundPool.load(context, R.raw.sfx_skill_bite, 1)

        soundMap["attack"] = soundPool.load(context, R.raw.sfx_basic_attack, 1)
        soundMap["buy"] = soundPool.load(context, R.raw.sfx_buying, 1)
        soundMap["button"] = soundPool.load(context, R.raw.sfx_button, 1)
        soundMap["defence"] = soundPool.load(context, R.raw.sfx_defence, 1)
        soundMap["decline"] = soundPool.load(context, R.raw.sfx_decline, 1)
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

    override fun mute() {
        // Nothing
    }

    override fun unMute() {
        // Nothing
    }
}