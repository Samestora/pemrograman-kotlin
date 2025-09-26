package com.sgdc.roguelike

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _player = MutableLiveData(
        Player(100, 100, 10, 5, 20, 20)
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

    fun playerHeal(amount: Int) {
        _player.value?.let { player ->
            player.health = (player.health + amount).coerceAtMost(player.maxHealth)
            _player.value = player // trigger LiveData update
        }
    }

}

