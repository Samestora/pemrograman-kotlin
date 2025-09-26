package com.sgdc.roguelike


import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object SfxManager : Sound {
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    override fun init(context: Context) {
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
        name?.let {
            val soundId = soundMap[it]
            if (soundId != null) {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    override fun stop(name: String?) {
        // Do nothing
    }

    override fun release() {
        soundPool.release()
    }
}
