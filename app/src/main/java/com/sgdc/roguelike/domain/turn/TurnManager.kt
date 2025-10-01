package com.sgdc.roguelike.domain.turn

class TurnManager {
    var currentTurn: Turn = Turn.PLAYER

    fun nextTurn(action: PlayerAction): TurnResult {
        return when (currentTurn) {
            Turn.PLAYER -> {
                currentTurn = Turn.MONSTER
                TurnResult.Player(action)
            }
            Turn.MONSTER -> {
                currentTurn = Turn.PLAYER
                TurnResult.Monster(decideMonsterAction())
            }
        }
    }
    fun decideMonsterAction(): MonsterAction {
        val roll = (1..10).random()
        return when {
            roll <= 5 -> MonsterAction.ATTACK
            roll <= 8 -> MonsterAction.SKILL
            else -> MonsterAction.DEFENCE
        }
    }

    fun switchTurn() {
        currentTurn = if (currentTurn == Turn.PLAYER) Turn.MONSTER else Turn.PLAYER
    }
}
