package com.sgdc.roguelike.domain.skill

import com.sgdc.roguelike.domain.character.Character
import com.sgdc.roguelike.domain.character.Player

interface Skill {
    val name: String
    val description: String
    val manaCost : Int
    // could be more complex return type than string...
    fun use(user: Character, target: Character): String
}

class Fireball : Skill {
    override val name = "Fireball"
    override val description = "Cast fireball"
    override val manaCost = 10
    override fun use(user: Character, target: Character): String {
        val damage = user.att * 2
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class Heal : Skill {
    override val name = "Heal"
    override val description = "Cast heal"
    override val manaCost = 20
    override fun use(user: Character, target: Character): String {
        if (user is Player) {
            val healAmount = 20
            val oldHealth = user.health
            user.health = (user.health + healAmount).coerceAtMost(user.maxHealth)
            return "Healed ${user.health - oldHealth} HP!"
        }
        return "$name had no effect."
    }
}