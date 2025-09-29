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
import com.sgdc.roguelike.domain.skill.Skill
import com.sgdc.roguelike.ui.viewmodel.MainViewModel
import com.sgdc.roguelike.ui.viewmodel.Screen

class BattleFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var skillsContainer: LinearLayout
    private lateinit var openSkillButton: Button
    private lateinit var battleMessage: TextView
    private lateinit var enemyHealthBar: ProgressBar
    private lateinit var attackButton: Button
    private lateinit var defenseButton: Button
    private lateinit var enemyName: TextView

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

        setupButtons()
        observeGame()
    }

    private fun setupButtons() {
        attackButton.setOnClickListener {
            gameViewModel.playerAttack()
            SfxManager.play("button")
        }
        defenseButton.setOnClickListener {
            gameViewModel.playerDefend()
            SfxManager.play("button")
        }
        openSkillButton.setOnClickListener {
            if (skillsContainer.visibility == View.GONE) {
                populateSkills()
                skillsContainer.visibility = View.VISIBLE
            } else {
                skillsContainer.visibility = View.GONE
            }
            SfxManager.play("button")
        }
    }

    private fun populateSkills() {
        skillsContainer.removeAllViews()
        val player = gameViewModel.player.value ?: return

        for (skill in player.skills) {
            val btn = Button(requireContext()).apply {
                text = skill.name
                setOnClickListener {
                    gameViewModel.playerUseSkill(skill)
                    skillsContainer.visibility = View.GONE
                    SfxManager.play("button")
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

