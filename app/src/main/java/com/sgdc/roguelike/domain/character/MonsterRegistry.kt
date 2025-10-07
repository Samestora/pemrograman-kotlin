package com.sgdc.roguelike.domain.character

import android.content.Context
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.skill.Bite
import com.sgdc.roguelike.domain.skill.DrainLife
import com.sgdc.roguelike.domain.skill.Fireball
import com.sgdc.roguelike.domain.skill.Heal
import com.sgdc.roguelike.domain.skill.Slash
import com.sgdc.roguelike.domain.skill.Thrust
import kotlin.collections.set

object MonsterRegistry {
    private val spriteMap = mutableMapOf<String, Int>()

    fun init(context: Context) {
        spriteMap.clear()

        spriteMap["floating_eye"] = R.drawable.floating_eye
    }

    private val monsters: List<() -> Monster> = listOf(
        { Monster("Floating Evil Eye", 50, 50, 8, 3, 0, 0, "floating_eye", listOf(Fireball(), Bite()))},
        { Monster("Goblin", 40, 40, 6, 2, 0, 0, "", listOf(DrainLife(), Heal())) },
        { Monster("Skeleton Warrior", 60, 60, 10, 4, 0, 0, "", listOf(Slash(), Thrust())) },
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

    fun getSpriteResId(spriteName: String): Int? {
        return spriteMap[spriteName]
    }
}
