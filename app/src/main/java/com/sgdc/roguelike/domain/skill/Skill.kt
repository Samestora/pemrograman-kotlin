package com.sgdc.roguelike.domain.skill

import com.sgdc.roguelike.domain.character.Character
import com.sgdc.roguelike.domain.character.Player

interface Skill {
    val name: String
    val description: String
    fun use(user: Character, target: Character)
}

class Fireball : Skill {
    override val name = "Fireball"
    override val description = ""
    override fun use(user: Character, target: Character) {
        val damage = user.att * 2
        target.takeDamage(damage)
    }
}

class Heal : Skill {
    override val name = "Heal"
    override val description = ""
    override fun use(user: Character, target: Character) {
        if (user is Player) {
            val healAmount = 20
            user.health = (user.health + healAmount).coerceAtMost(user.maxHealth)
        }
    }
}