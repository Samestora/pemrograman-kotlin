package com.sgdc.roguelike

interface GameObserver {
    fun onCharacterDamaged(character: Character, damage: Int)
    fun onCharacterDied(character: Character)
    fun onLevelUp(player: Player)
}
