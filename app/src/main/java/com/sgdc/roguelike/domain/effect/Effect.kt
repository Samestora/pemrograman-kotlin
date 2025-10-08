package com.sgdc.roguelike.domain.effect

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.penfeizhou.animation.gif.GifDrawable
import com.github.penfeizhou.animation.loader.ResourceStreamLoader
import com.sgdc.roguelike.App
import com.sgdc.roguelike.R

interface Effect {
    fun play(targetView: View)
}

class FireballEffect : Effect {
    override fun play(targetView: View) {
        val context = App.context
        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                targetView.width,
                targetView.height
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        val loader = ResourceStreamLoader(context, R.raw.fireball_effect)
        val drawable = GifDrawable(loader)
        imageView.setImageDrawable(drawable)

        val parent = targetView.parent as? FrameLayout
        parent?.addView(imageView)

        drawable.start()

        imageView.postDelayed({
            drawable.stop()
            parent?.removeView(imageView)
        }, 750)
    }
}

class AttackEffect : Effect {
    override fun play(targetView: View) {
        val context = App.context
        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                targetView.width,
                targetView.height
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        val loader = ResourceStreamLoader(context, R.raw.attack_effect)
        val drawable = GifDrawable(loader)
        imageView.setImageDrawable(drawable)

        val parent = targetView.parent as? FrameLayout
        parent?.addView(imageView)

        drawable.start()

        imageView.postDelayed({
            drawable.stop()
            parent?.removeView(imageView)
        }, 750)
    }
}

class HealEffect : Effect {
    override fun play(targetView: View) {
        val context = App.context
        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                targetView.width,
                targetView.height
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        val loader = ResourceStreamLoader(context, R.raw.heal_effect)
        val drawable = GifDrawable(loader)
        imageView.setImageDrawable(drawable)

        val parent = targetView.parent as? FrameLayout
        parent?.addView(imageView)

        drawable.start()
        imageView.postDelayed({
            drawable.stop()
            parent?.removeView(imageView)
        }, 750)
    }
}

class FlameBurst : Effect {
    override fun play(targetView: View) {
        val context = App.context
        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                targetView.width,
                targetView.height
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        val loader = ResourceStreamLoader(context, R.raw.flame_burst_effect)
        val drawable = GifDrawable(loader)
        imageView.setImageDrawable(drawable)

        val parent = targetView.parent as? FrameLayout
        parent?.addView(imageView)

        drawable.start()
        imageView.postDelayed({
            drawable.stop()
            parent?.removeView(imageView)
        }, 750)
    }
}