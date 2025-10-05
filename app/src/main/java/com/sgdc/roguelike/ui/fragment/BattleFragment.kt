package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sgdc.roguelike.ui.viewmodel.GameViewModel
import com.sgdc.roguelike.R
import com.sgdc.roguelike.domain.bgm.SfxManager
import com.sgdc.roguelike.domain.turn.PlayerAction
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen
import androidx.core.view.isGone
import com.sgdc.roguelike.domain.character.MonsterRegistry
import com.sgdc.roguelike.domain.item.Item
import com.sgdc.roguelike.domain.skill.Skill
import com.sgdc.roguelike.domain.turn.Turn
import com.sgdc.roguelike.domain.turn.TurnManager
import com.sgdc.roguelike.domain.turn.TurnResult
import com.sgdc.roguelike.effect.VisualEffect

class BattleFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var skillsContainer: LinearLayout
    private lateinit var openSkillButton: Button
    private lateinit var battleMessage: TextView
    private lateinit var enemyHealthBar: ProgressBar
    private lateinit var attackButton: Button
    private lateinit var defenseButton: Button
    private lateinit var nextButton: Button
    private lateinit var enemyName: TextView
    private lateinit var enemySprite: ImageView

    //    PLAYER
    private lateinit var playerHealthBar: ProgressBar
    private lateinit var playerHealthText: TextView
    private lateinit var playerManaBar: ProgressBar
    private lateinit var playerManaText: TextView

    private var turnManager = TurnManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameViewModel.spawnMonster()
        return inflater.inflate(R.layout.fragment_battle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enemySprite = view.findViewById(R.id.enemySprite)
        skillsContainer = view.findViewById(R.id.skillsContainer)
        openSkillButton = view.findViewById(R.id.openSkillButton)
        battleMessage = view.findViewById(R.id.battleMessage)
        enemyHealthBar = view.findViewById(R.id.enemyHealthbar)
        attackButton = view.findViewById(R.id.attackButton)
        defenseButton = view.findViewById(R.id.defenseButton)
        enemyName = view.findViewById(R.id.enemyName)
        nextButton = view.findViewById(R.id.nextBtn)

        playerHealthBar = view.findViewById(R.id.playerHealthbar)
        playerHealthText = view.findViewById(R.id.playerHealthText)
        playerManaBar = view.findViewById(R.id.playerManaBar)
        playerManaText = view.findViewById(R.id.playerManaText)

        setupButtons()
        observeGame()
    }

    private fun setupButtons() {
        attackButton.setOnClickListener {
            gameViewModel.performPlayerAction(PlayerAction.ATTACK)
            setActionButtonsVisible(false)
            setNextButtonVisible(true)
            SfxManager.play("attack")
            VisualEffect.play("attack", enemySprite)
        }
        defenseButton.setOnClickListener {
            gameViewModel.performPlayerAction(PlayerAction.DEFENCE)
            setActionButtonsVisible(false)
            setNextButtonVisible(true)
            SfxManager.play("button")
        }
        openSkillButton.setOnClickListener {
            if (skillsContainer.isGone) {
                populateSkills()
                skillsContainer.visibility = View.VISIBLE
            } else {
                skillsContainer.visibility = View.GONE
            }
            SfxManager.play("button")
        }
        nextButton.setOnClickListener {
            gameViewModel.performMonsterAction()
            turnManager.switchTurn()
            setActionButtonsVisible(true)
            setNextButtonVisible(false)
        }
    }

    private fun populateSkills() {
        skillsContainer.removeAllViews()
        val player = gameViewModel.player.value ?: return
        for (skill in player.skills) {
            val btn = Button(requireContext()).apply {
                text = skill.name
                setOnClickListener {
                    val currentPlayer = gameViewModel.player.value ?: return@setOnClickListener
                    val manaRemain = currentPlayer.mana
                    if(manaRemain >= (skill.manaCost)){
                        gameViewModel.performPlayerAction(PlayerAction.SKILL, skill)
                        skillsContainer.visibility = View.GONE
                        setActionButtonsVisible(false)
                        setNextButtonVisible(true)
                        SfxManager.play(skill.name)
                        if(skill.name.lowercase() != "heal"){
                            VisualEffect.play(skill.name, enemySprite)
                        }
                    }
                    else{
                        battleMessage.text = "Didn't have enough mana"
                        SfxManager.play("decline")
                    }
                }
            }
            skillsContainer.addView(btn)
        }
    }


    private fun observeGame() {
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            if (player != null) {
                playerHealthBar.max = player.maxHealth
                playerHealthBar.progress = player.health
                playerManaBar.max = player.maxMana
                playerManaBar.progress = player.mana

                if (player.health <= 0) {
                    mainViewModel.navigateTo(Screen.Death)
                }
            }
        }

        gameViewModel.monster.observe(viewLifecycleOwner) { monster ->
            if (monster != null) {
                enemyName.text = monster.name
                // Use the registry to get the resource ID from the sprite name
                val spriteResId = MonsterRegistry.getSpriteResId(monster.spriteName)
                if (spriteResId != null) {
                    enemySprite.setImageResource(spriteResId)
                } else {
                    // Optional: Set a default image if the sprite is not found
                    enemySprite.setImageResource(R.drawable.default_monster_sprite)
                }

                enemyHealthBar.max = monster.maxHealth
                enemyHealthBar.progress = monster.health

                if (monster.health <= 0) {
                    mainViewModel.navigateTo(Screen.Gacha)
                }
            }
        }

        gameViewModel.battleMessage.observe(viewLifecycleOwner) { message ->
            battleMessage.text = message
        }
    }

    private fun setActionButtonsVisible(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        attackButton.visibility = visibility
        defenseButton.visibility = visibility
        openSkillButton.visibility = visibility
        skillsContainer.visibility = visibility
    }

    private fun setNextButtonVisible(visible: Boolean) {
        nextButton.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

