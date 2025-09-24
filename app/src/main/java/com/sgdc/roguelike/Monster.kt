package com.sgdc.roguelike

class Monster (health:Int,
               att:Int,
               maxHealth:Int,
               def:Int): Character(health, maxHealth,att, def){
    override fun attack(target: Character) {
        val damage = (this.att - target.def).coerceAtLeast(1)
        target.takeDamage(damage)
    }

    override fun takeDamage(damage: Int) {
        health -= damage
        if (health < 0) health = 0
    }

}