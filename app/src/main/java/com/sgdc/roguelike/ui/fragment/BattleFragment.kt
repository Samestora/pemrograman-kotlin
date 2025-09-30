package com.sgdc.roguelike.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.sgdc.roguelike.domain.turn.TurnManager
import com.sgdc.roguelike.domain.turn.TurnResult

class BattleFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var skillsContainer: LinearLayout
    private lateinit var openSkillButton: Button
    private lateinit var battleMessage: TextView
    private lateinit var enemyHealthBar: ProgressBar
    private lateinit var attackButton: Button
    private lateinit var defenseButton: Button
    private lateinit var nextBtn: Button
    private lateinit var enemyName: TextView
    private lateinit var debugBar: TextView

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

        skillsContainer = view.findViewById(R.id.skillsContainer)
        openSkillButton = view.findViewById(R.id.openSkillButton)
        battleMessage = view.findViewById(R.id.battleMessage)
        enemyHealthBar = view.findViewById(R.id.enemyHealthbar)
        attackButton = view.findViewById(R.id.attackButton)
        defenseButton = view.findViewById(R.id.defenseButton)
        enemyName = view.findViewById(R.id.enemyName)
        debugBar = view.findViewById(R.id.debugBar)
        nextBtn = view.findViewById(R.id.nextBtn)

        setupButtons()
        observeGame()
    }

    private fun setupButtons() {
        attackButton.setOnClickListener {
            gameViewModel.performAction(PlayerAction.ATTACK)
            SfxManager.play("attack")
        }
        defenseButton.setOnClickListener {
            gameViewModel.performAction(PlayerAction.DEFENCE)
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
        nextBtn.setOnClickListener {
        }
    }

    private fun populateSkills() {
        skillsContainer.removeAllViews()
        val player = gameViewModel.player.value ?: return

        for (skill in player.skills) {
            val btn = Button(requireContext()).apply {
                text = skill.name
                setOnClickListener {
                    gameViewModel.performAction(PlayerAction.SKILL, skill)
                    skillsContainer.visibility = View.GONE
                    SfxManager.play(skill.name)
                }
            }
            skillsContainer.addView(btn)
        }
    }


    private fun observeGame() {
        gameViewModel.player.observe(viewLifecycleOwner) { player ->
            // TODO: if you add a player HP bar, update it here
        }

        gameViewModel.monster.observe(viewLifecycleOwner) { monster ->
            if (monster != null) {
                enemyName.text = monster.name
                enemyHealthBar.max = monster.maxHealth
                enemyHealthBar.progress = monster.health
                debugBar.text = monster.maxHealth.toString()

                if (monster.health <= 0) {
                    mainViewModel.navigateTo(Screen.Gacha)
                }
            }
        }

        gameViewModel.battleMessage.observe(viewLifecycleOwner) { message ->
            battleMessage.text = message
        }
    }
}

