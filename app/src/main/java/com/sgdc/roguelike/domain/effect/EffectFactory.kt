package com.sgdc.roguelike.domain.effect

object EffectFactory {
    fun createEffect(name: String): Effect?{
        return when(name.lowercase()){
            "fireball" -> FireballEffect()
            "heal"-> HealEffect()
            "attack" -> AttackEffect()
            "flame burst" -> FlameBurst()
            "bite" -> Bite()
            "drain life" -> DrainLife()
            "thunder strike" -> ThunderStrike()
            "ice spike" -> IceSpike()
            "slash" -> Slash()
            "thrust" -> Thrust()
            else -> null
        }
    }
}