package com.sgdc.roguelike.domain.character

import com.sgdc.roguelike.domain.skill.Skill

class Monster(
    name: String,
    health: Int,
    maxHealth: Int,
    att: Int,
    def: Int,
    mana: Int,
    maxMana: Int,
    val spriteName: String,
    val skills: List<Skill> = emptyList()
) : Character(name, health, maxHealth, att, def, mana, maxMana) {

    override fun attack(target: Character) {
        val damage = (this.att - target.def).coerceAtLeast(1)
        target.takeDamage(damage)
    }

    override fun takeDamage(damage: Int) {
        health -= damage
        if (health < 0) health = 0
    }
}