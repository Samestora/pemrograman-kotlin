package com.sgdc.roguelike

import kotlin.reflect.KClass

object SkillRegistry {
    private val skills: List<() -> Skill> = listOf(
        { Fireball() },
        { Heal() }
    )

    fun randomSkill(): Skill {
        return skills.random().invoke()
    }
}
