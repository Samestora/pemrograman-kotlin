package com.sgdc.roguelike.domain.character

import com.sgdc.roguelike.domain.item.InventoryItem
import com.sgdc.roguelike.domain.item.Item
import com.sgdc.roguelike.domain.skill.Skill

class Player (name: String,
              health: Int,
              maxHealth: Int,
              att: Int,
              def: Int,
              mana:Int,
              maxMana: Int): Character(name, health, maxHealth, att, def, mana, maxMana){

    val skills = mutableListOf<Skill>()
    val items = mutableListOf<InventoryItem>()


    override fun attack(target: Character) {
        val damage = (this.att - target.def).coerceAtLeast(1)
        target.takeDamage(damage)
    }

    override fun takeDamage(damage: Int) {
        health -= damage
        if (health < 0) health = 0
    }

    fun addSkill(skill: Skill) {
        skills.add(skill)
    }

    fun removeSkill (skill: Skill){
        skills.remove(skill)
    }

    fun useSkill(index: Int, target: Character) {
        if (index in skills.indices) {
            val skill = skills[index]
            skill.use(this, target)
        }
    }

    fun addItem(item: Item){
        val existing = items.find { it.item::class == item::class }
        if (existing != null) {
            existing.amount += 1
        } else {
            items.add(InventoryItem(item, 1))
        }
    }

    fun removeItem(item: Item){
        val existing = items.find { it.item::class == item::class }
        existing?.let {
            it.amount -= 1
            if (it.amount <= 0) items.remove(it)
        }
    }
}