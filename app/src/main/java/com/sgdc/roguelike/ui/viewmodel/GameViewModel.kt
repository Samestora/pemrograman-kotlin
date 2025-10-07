package com.sgdc.roguelike.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgdc.roguelike.domain.character.Monster
import com.sgdc.roguelike.domain.character.MonsterRegistry
import com.sgdc.roguelike.domain.character.MonsterRegistry.init
import com.sgdc.roguelike.domain.character.Player
import com.sgdc.roguelike.domain.item.Item
import com.sgdc.roguelike.domain.save.GameProgress
import com.sgdc.roguelike.domain.skill.Skill
import com.sgdc.roguelike.domain.skill.SkillRegistry
import com.sgdc.roguelike.domain.turn.MonsterAction
import com.sgdc.roguelike.domain.turn.PlayerAction
import com.sgdc.roguelike.domain.turn.Turn
import com.sgdc.roguelike.domain.turn.TurnManager
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

    private val _stageFloor = MutableLiveData(1)
    val stageFloor: LiveData<Int> = _stageFloor
    private val turnManager = TurnManager()

    // --------------------
    // BATTLE FLOW
    // --------------------
    fun spawnMonster() {
        turnManager.resetTurn()
        val baseMonster = MonsterRegistry.randomMonster()
        val buffed = MonsterRegistry.buffMonster(baseMonster, _stageFloor.value ?: 1)
        _monster.value = buffed
        _battleMessage.value = "${_monster.value?.name} has appeared!"
        _battleFinished.value = false
    }

    fun finishBattle() {
        val player = _player.value?: return
        _battleFinished.value = true
        _monster.value = null
        player.money += (5..20).random() + ((_stageFloor.value?: 1) + 5)
        _stageFloor.value = (_stageFloor.value ?: 1) + 1
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
        println("Gameview Mana: " + player.mana)
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

    fun monsterDefend() {
        val currentMonster = _monster.value ?: return
        _battleMessage.value = "${currentMonster.name} braces for impact!"
    }

    fun monsterUseSkill() {
        val currentMonster = _monster.value ?: return
        val currentPlayer = _player.value ?: return

        val skills = currentMonster.skills
        if (skills.isEmpty()) {
            currentMonster.attack(currentPlayer)
            _battleMessage.value = "${currentMonster.name} attacked ${currentPlayer.name}!"
            return
        }
        // Pilih skill secara acak (atau sesuai logika tertentu)
        val skill = skills.random()
        val resultMessage = skill.use(currentMonster, currentPlayer)
        _battleMessage.value = "${currentMonster.name} used ${skill.name}! $resultMessage"
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
//    fun <T : Skill> playerUseSkill(skillClass: KClass<T>) {
//        _player.value?.let { player ->
//            val skill = player.skills.firstOrNull { skillClass.isInstance(it) }
//            if (skill != null) {
//                (skill as T).use(player, player)
//                updatePlayer(player)
//            } else {
//                println("Player does not have skill: ${skillClass.simpleName}")
//            }
//        }
//    }

    // --------------------
    // REST ACTIONS
    // --------------------
    fun playerRest() {
        _player.value?.let { player ->
            player.health = (player.health + 20).coerceAtMost(player.maxHealth)
            player.mana = (player.mana + 10).coerceAtMost(player.maxMana)
            updatePlayer(player)
        }
    }

    fun isAllSkillsAvailable() : Boolean {
        val player = _player.value ?: return false
        val newSkill = SkillRegistry.randomSkillExcluding(player.skills)

        return newSkill != null
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

    fun playerAddItem(item: Item): Boolean {
        val player = _player.value ?: return false
        if((item.price - player.money) <= 0){
            player.money -= item.price
            player.addItem(item)
            updatePlayer(player)
            return true
        }
        else{
            return false
        }
    }

    fun playerBuyRandomSkill(): Boolean {
        val player = _player.value ?: return false
        if (player.money >= 100) {
            player.money -= 100
            grantRandomSkill()
            return true
        } else {
            return false
        }
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

    fun handlePlayerAction(action: PlayerAction, skill: Skill?, item: Item?) {
        when (action) {
            PlayerAction.ATTACK -> playerAttack()
            PlayerAction.DEFENCE -> playerDefend()
            PlayerAction.SKILL -> skill?.let { playerUseSkill(it) }
            PlayerAction.ITEM -> item?.let { playerUseItem(it) }
        }
    }

    fun handleMonsterAction(action: MonsterAction) {
        when (action) {
            MonsterAction.ATTACK -> monsterAttack()
            MonsterAction.DEFENCE -> monsterDefend()
            MonsterAction.SKILL -> monsterUseSkill()
        }
    }

    fun performPlayerAction(action: PlayerAction, skill: Skill? = null, item: Item? = null) {
        if (turnManager.currentTurn != Turn.PLAYER) return
        handlePlayerAction(action,skill,item)
        turnManager.switchTurn()
    }

    fun performMonsterAction() {
        if (turnManager.currentTurn != Turn.MONSTER) return
        val action = turnManager.decideMonsterAction()
        handleMonsterAction(action)
        turnManager.switchTurn()
    }
}