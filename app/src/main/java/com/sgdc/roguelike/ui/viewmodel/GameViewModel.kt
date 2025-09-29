package com.sgdc.roguelike.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgdc.roguelike.domain.character.Monster
import com.sgdc.roguelike.domain.character.Player
import com.sgdc.roguelike.domain.item.Item
import com.sgdc.roguelike.domain.skill.Skill
import com.sgdc.roguelike.domain.skill.SkillRegistry
import kotlin.collections.plusAssign
import kotlin.reflect.KClass

class GameViewModel : ViewModel() {
    private val _player = MutableLiveData(
        Player("Player", 50, 100, 10, 5, 20, 20)
    )
    val player: LiveData<Player> = _player

    private val _monster = MutableLiveData<Monster?>()
    val monster: LiveData<Monster?> = _monster

    private val _battleMessage = MutableLiveData<String>()
    val battleMessage: LiveData<String> = _battleMessage

    private val _battleFinished = MutableLiveData<Boolean>()
    val battleFinished: LiveData<Boolean> = _battleFinished

    // --------------------
    // BATTLE FLOW
    // --------------------
    fun spawnMonster() {
        val monsters = listOf(
            Monster("Floating Evil Eye", 50, 50, 8, 3, 0, 0),
            Monster("Goblin", 40, 40, 6, 2, 0, 0),
            Monster("Skeleton Warrior", 60, 60, 10, 4, 0, 0),
        )
        _monster.value = monsters.random()
        _battleMessage.value = "A wild ${_monster.value?.name} appeared!"
        _battleFinished.value = false
    }

    fun finishBattle() {
        _monster.value = null
        _battleFinished.value = true
        _battleMessage.value = "Battle finished!"
    }

    fun resetBattle() {
        _battleFinished.value = false
    }

    // --------------------
    // STATE UPDATES
    // --------------------
    fun updatePlayer(player: Player) {
        _player.value = player
    }

    fun updateMonster(monster: Monster) {
        _monster.value = monster
        if (monster.health <= 0) {
            finishBattle()
        }
    }

    // --------------------
    // PLAYER ACTIONS
    // --------------------
    fun playerAttack() {
        val currentMonster = _monster.value ?: return
        val currentPlayer = _player.value ?: return

        val damage = (currentPlayer.att - currentMonster.def).coerceAtLeast(1)
        currentMonster.takeDamage(damage)

        updateMonster(currentMonster)
        _battleMessage.value = "${currentPlayer.name} attacks ${currentMonster.name} for $damage damage!"
    }

    fun playerDefend() {
        _battleMessage.value = "${_player.value?.name} braces for impact!"
        // You can implement a "defending" state to reduce next damage if needed
    }

    fun playerUseSkill(skill: Skill) {
        val player = _player.value ?: return
        if(player.mana > 0){
            player.mana -= skill.manaCost
            if(player.mana <= 0){
                player.mana = 0
            }
        }else{
            _battleMessage.value = "Didn't have enough mana"
            return
        }
        val monster = _monster.value ?: return

        val resultMessage = skill.use(player, monster)
        updatePlayer(player)
        updateMonster(monster)

        _battleMessage.value = "Used ${skill.name} → $resultMessage"
    }

    // --------------------
    // MONSTER ACTIONS
    // --------------------
    fun monsterAttack() {
        val currentMonster = _monster.value ?: return
        val currentPlayer = _player.value ?: return

        val damage = (currentMonster.att - currentPlayer.def).coerceAtLeast(1)
        currentMonster.attack(currentPlayer)

        updatePlayer(currentPlayer)
        _battleMessage.value = "${currentMonster.name} hits back for $damage damage!"
    }

    fun <T : Skill> playerAddSkill(skillClass: KClass<T>) {
        _player.value?.let { player ->
            val skill = skillClass.constructors.first().call() // create instance
            player.addSkill(skill)
            updatePlayer(player) // trigger observers
        }
    }


    //    TODO make generic playerUseSkill(SkillName) instead it should be a query => { it is SkillName }
    //      Can only be used from player to player DONT TOUCH!!!!!
    fun <T : Skill> playerUseSkill(skillClass: KClass<T>) {
        _player.value?.let { player ->
            val skill = player.skills.firstOrNull { skillClass.isInstance(it) }
            if (skill != null) {
                (skill as T).use(player, player)
                updatePlayer(player)
            } else {
                println("Player does not have skill: ${skillClass.simpleName}")
            }
        }
    }

    fun playerRest() {
        _player.value?.let { player ->
            player.health = (player.health + 20).coerceAtMost(player.maxHealth)
            updatePlayer(player)
        }
    }

    fun grantRandomSkill(): Skill? {
        val player = _player.value ?: return null
        val newSkill = SkillRegistry.randomSkillExcluding(player.skills)

        return if (newSkill != null) {
            player.addSkill(newSkill)
            updatePlayer(player)
            newSkill
        } else {
            // All skills already collected
            null
        }
    }

    fun playerAddItem(item: Item) {
        val player = _player.value ?: return
        player.addItem(item)
        updatePlayer(player)
    }

    fun playerUseItem(item: Item) {
        val player = _player.value ?: return
        item.useItem(player)
        player.removeItem(item)
        updatePlayer(player)
    }


    fun gachaStat(stat: String, amount: Int) {
        when (stat) {
            "Attack" -> _player.value?.let{ player ->
                player.att += amount
                _player.value = player
            }
            "Max Health" -> _player.value?.let{ player ->
                player.maxHealth += amount
                _player.value = player
            }
            "Max Mana" -> _player.value?.let{ player ->
                player.maxMana += amount
                _player.value = player
            }
            "Defense" -> _player.value?.let{ player ->
                player.def += amount
                _player.value = player
            }
        }
    }
}