package com.sgdc.roguelike

class Player (health: Int,
              maxHealth: Int,
              att: Int,
              def: Int,
    mana:Int) : Character(health, maxHealth, att, def){

    var mana:Int = mana

    var maxMana:Int = mana

    val skills = mutableListOf<Skill>()

    constructor(health:Int,
                maxHealth:Int,
                att:Int,
                def:Int,
        mana:Int,
        maxMana:Int):
            this(health, maxHealth, att, def, mana){
        this.att = att
        this.def = def
        this.health = health
        this.maxHealth = maxHealth
        this.mana = mana
        this.maxMana = maxMana
    }

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

    fun getAtt(): Int {
        return this.att
    }

    fun getHealth(): Int{
        return this.health
    }

    fun getMaxHeatlh(): Int{
        return this.maxHealth
    }

    fun getDef(): Int{
        return this.def
    }

    fun getMana():Int{
        return this.mana
    }

    fun getMaxMana(): Int{
        return this.maxMana
    }


}
