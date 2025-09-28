package com.sgdc.roguelike.domain.character

class Monster(
    name: String,
    health: Int,
    maxHealth: Int,
    att: Int,
    def: Int
) : Character(name, health, maxHealth, att, def) {

    override fun attack(target: Character) {
        val damage = (this.att - target.def).coerceAtLeast(1)
        target.takeDamage(damage)
    }

    override fun takeDamage(damage: Int) {
        health -= damage
        if (health < 0) health = 0
    }
}
