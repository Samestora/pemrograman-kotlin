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
        spriteMap["goblin"] = R.drawable.goblin
        spriteMap["skeleton_warrior"] = R.drawable.skeleton_warrior
        spriteMap["dracula"] = R.drawable.dracula
    }

    private val monsters: List<() -> Monster> = listOf(
        { Monster("Floating Evil Eye", 50, 50, 8, 3, 0, 0, "floating_eye", false ,listOf(Fireball(), Bite()))},
        { Monster("Goblin", 40, 40, 6, 2, 0, 0, "goblin", false, listOf(DrainLife(), Heal())) },
        { Monster("Skeleton Warrior", 60, 60, 10, 4, 0, 0, "skeleton_warrior", false, listOf(Slash(), Thrust())) },
        { Monster("Dracula", 150, 150, 8, 2, 0, 0, "dracula", true, listOf(Heal(), DrainLife(), Bite())) },
    )

    fun getBuffedRandomMonster(stageFloor: Int, stageArea: Int): Monster {
        // 1. Get a fresh, base monster
        val baseMonster = monsters
            .filter { !it().isBoss }
            .random()
            .invoke()

        // 2. Apply buffs and return it
        return buffMonster(baseMonster, stageFloor, stageArea)
    }

    fun getBuffedRandomBoss(stageFloor: Int, stageArea: Int): Monster {
        val baseBoss = monsters
            .filter { it().isBoss }
            .random()
            .invoke()
        return buffMonster(baseBoss, stageFloor, stageArea)
    }

    fun randomMonster(): Monster {
        return monsters
            .filter { !it().isBoss }
            .random()
            .invoke()
    }
    fun randomBoss(): Monster {
        return monsters
            .filter { it().isBoss }
            .random()
            .invoke()
    }
    fun buffMonster(monster: Monster, stageFloor: Int, stageArea: Int) : Monster {
        return monster.apply {
            maxHealth += stageFloor + stageArea
            health = maxHealth // Always start with full health
            att += (stageFloor % 2) + stageArea // Using /2 for attack to scale a bit slower
            def += (stageFloor % 2) + stageArea // Using /2 for defense to scale a bit slower
        }
    }

    fun getSpriteResId(spriteName: String): Int? {
        return spriteMap[spriteName]
    }
}
