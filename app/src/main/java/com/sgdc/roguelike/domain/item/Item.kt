package com.sgdc.roguelike.domain.item

import com.sgdc.roguelike.domain.character.Character

interface Item {
    val name : String
    val description : String
    fun useItem(user : Character)
}

data class InventoryItem(
    val item: Item,
    var amount: Int
)

class HealthPotion:Item{
    override val name = "Health Potion"
    override val description = ""
    override fun useItem(user: Character) {
        user.health = (user.health + 20).coerceAtMost(user.maxHealth)
    }
}

class ManaPotion:Item{
    override val name = "Mana Potion"
    override val description = ""
    override fun useItem(user: Character) {
        user.mana = (user.health + 20).coerceAtMost(user.maxMana)
    }
}
