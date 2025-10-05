package com.sgdc.roguelike.effect

object EffectFactory {
    fun createEffect(name: String): Effect?{
        return when(name.lowercase()){
            "fireball" -> FireballEffect()
            "heal"-> HealEffect()
            "attack" -> AttackEffect()
            else -> null
        }
    }
}