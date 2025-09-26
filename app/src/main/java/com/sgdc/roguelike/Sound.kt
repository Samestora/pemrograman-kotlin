package com.sgdc.roguelike

import android.content.Context

interface Sound {
    fun play(name: String?)
    fun stop(name:String? = null)
    fun release()
    fun init(context: Context)
}