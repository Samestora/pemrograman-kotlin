package com.sgdc.roguelike.domain.character

object MonsterRegistry {
    private val monsters: List<() -> Monster> = listOf(
        { Monster("Floating Evil Eye", 50, 50, 8, 3, 0, 0) },
        { Monster("Goblin", 40, 40, 6, 2, 0, 0) },
        { Monster("Skeleton Warrior", 60, 60, 10, 4, 0, 0) },
    )

    fun randomMonster(): Monster {
        return monsters.random().invoke()
    }

    fun buffMonster(monster: Monster, stageFloor: Int) : Monster {
        monster.health += (stageFloor)
        monster.maxHealth += (stageFloor)
        monster.att += (stageFloor % 2)
        monster.def += (stageFloor % 2)

        return monster
    }
}
