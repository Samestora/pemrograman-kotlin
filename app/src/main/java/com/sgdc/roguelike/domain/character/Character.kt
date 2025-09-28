package com.sgdc.roguelike.domain.character

abstract class Character(
    var name: String,
    var health: Int,
    var maxHealth: Int,
    var att: Int,
    var def: Int,
    var mana: Int
) {
    abstract fun attack(target: Character)
    abstract fun takeDamage(damage: Int)
}