package com.sgdc.roguelike.domain.character

abstract class Character(
    var health: Int,
    var maxHealth: Int,
    var att: Int,
    var def: Int
) {
    abstract fun attack(target: Character)
    abstract fun takeDamage(damage: Int)
}