package com.sgdc.roguelike.domain.skill

object SkillRegistry {
    private val skills: List<() -> Skill> = listOf(
        { Fireball() },
        { Heal() }
    )

    fun randomSkill(): Skill {
        return skills.random().invoke()
    }
}