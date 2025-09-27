package com.sgdc.roguelike.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgdc.roguelike.domain.character.Monster
import com.sgdc.roguelike.domain.character.Player
import com.sgdc.roguelike.domain.skill.Skill
import com.sgdc.roguelike.domain.skill.SkillRegistry
import kotlin.reflect.KClass

class GameViewModel : ViewModel() {
    private val _player = MutableLiveData(
        Player(50, 100, 10, 5, 20, 20)
    )
    val player: LiveData<Player> = _player

    private val _monster = MutableLiveData(
        Monster(50, 50, 8, 3) // simple example
    )
    val monster: LiveData<Monster> = _monster

    private val _battleFinished = MutableLiveData<Boolean>()
    val battleFinished: LiveData<Boolean> = _battleFinished

    fun finishBattle() {
        _battleFinished.value = true
    }

    fun resetBattle() {
        _battleFinished.value = false
    }

    fun playerAttack() {
        _player.value?.let { player ->
            _monster.value?.let { monster ->
                player.attack(monster)
                if (monster.health <= 0 || player.health <=0) {
                    finishBattle()
                }
                _player.value = player
                _monster.value = monster
            }
        }
    }

    fun <T : Skill> playerAddSkill(skillClass: KClass<T>) {
        _player.value?.let { player ->
            val skill = skillClass.constructors.first().call() // create instance
            player.addSkill(skill)
            _player.value = player // trigger observers
        }
    }


    //    TODO make generic playerUseSkill(SkillName) instead it should be a query => { it is SkillName }
    //      Can only be used from player to player DONT TOUCH!!!!!
    fun <T : Skill> playerUseSkill(skillClass: KClass<T>) {
        _player.value?.let { player ->
            val skill = player.skills.firstOrNull { skillClass.isInstance(it) }
            if (skill != null) {
                (skill as T).use(player, player)
                _player.value = player
            } else {
                println("Player does not have skill: ${skillClass.simpleName}")
            }
        }
    }

    fun playerRest() {
        _player.value?.let { player ->
            player.health += 20
            _player.value = player
        }
    }

    fun grantRandomSkill(): Skill {
        val newSkill = SkillRegistry.randomSkill()
        _player.value?.let { player ->
            player.addSkill(newSkill)
            _player.value = player
        }
        return newSkill
    }
}