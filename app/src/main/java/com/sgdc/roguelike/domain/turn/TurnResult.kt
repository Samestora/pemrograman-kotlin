package com.sgdc.roguelike.domain.turn

sealed class TurnResult {
    data class Player(val action: PlayerAction?) : TurnResult()
    data class Monster(val action: MonsterAction) : TurnResult()
}