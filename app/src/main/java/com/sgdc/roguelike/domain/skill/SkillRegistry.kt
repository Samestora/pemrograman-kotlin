package com.sgdc.roguelike.domain.skill

object SkillRegistry {
    private val skills: List<() -> Skill> = listOf(
        { Fireball() },
        { Heal() }
    )

    fun allSkills(): List<Skill> = skills.map { it.invoke() }

    fun randomSkillExcluding(owned: List<Skill>): Skill? {
        val ownedNames = owned.map { it::class } // track by type
        val available = skills
            .map { it.invoke() }
            .filter { it::class !in ownedNames }

        return if (available.isEmpty()) null else available.random()
    }
}
