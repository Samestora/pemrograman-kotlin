package com.sgdc.roguelike.domain.effect

import android.view.View

object VisualEffect {
    fun play(name: String, targetView: View) {
        val effect = EffectFactory.createEffect(name)
        effect?.play(targetView)
    }
}