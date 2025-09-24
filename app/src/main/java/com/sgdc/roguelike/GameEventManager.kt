package com.sgdc.roguelike

class GameEventManager {
    private val observers = mutableListOf<GameObserver>()

    fun addObserver(observer: GameObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: GameObserver) {
        observers.remove(observer)
    }

    fun notifyCharacterDamaged(character: Character, damage: Int) {
        observers.forEach { it.onCharacterDamaged(character, damage) }
    }

    fun notifyCharacterDied(character: Character) {
        observers.forEach { it.onCharacterDied(character) }
    }

    fun notifyLevelUp(player: Player) {
        observers.forEach { it.onLevelUp(player) }
    }
}
