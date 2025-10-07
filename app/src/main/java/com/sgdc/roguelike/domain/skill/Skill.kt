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
    override val manaCost = 11
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

class IceSpike : Skill {
    override val name = "Ice Spike"
    override val description = "Launch a sharp icicle to pierce the enemy."
    override val manaCost = 8

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 1.8).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class ThunderStrike : Skill {
    override val name = "Thunder Strike"
    override val description = "Call down lightning to strike the target."
    override val manaCost = 18

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 2.7).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class DrainLife : Skill {
    override val name = "Drain Life"
    override val description = "Steal life from the target to heal yourself."
    override val manaCost = 15

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 1.5).toInt()
        target.takeDamage(damage)
        val healAmount = (damage * 0.5).toInt()
        val oldHealth = user.health
        user.health = (user.health + healAmount).coerceAtMost(user.maxHealth)
        return "drained $damage HP from ${target.name} and recovered ${user.health - oldHealth} HP!"
    }
}

class FlameBurst : Skill {
    override val name = "Flame Burst"
    override val description = "An explosive burst of flame that deals heavy damage."
    override val manaCost = 14

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 2.3).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class Bite : Skill {
    override val name = "Bite"
    override val description = "A basic but fierce bite attack."
    override val manaCost = 0

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 1.2).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class Slash : Skill {
    override val name = "Slash"
    override val description = "A strong slash attack."
    override val manaCost = 3

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 1.35).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}

class Thrust: Skill{
    override val name = "Slash"
    override val description = "A strong thrust attack."
    override val manaCost = 5

    override fun use(user: Character, target: Character): String {
        val damage = (user.att * 1.6).toInt()
        target.takeDamage(damage)
        return "Dealt $damage damage to ${target.name}!"
    }
}