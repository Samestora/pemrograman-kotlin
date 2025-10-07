package com.sgdc.roguelike.ui.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.sgdc.roguelike.domain.character.MonsterRegistry
import com.sgdc.roguelike.domain.turn.TurnManager
import com.sgdc.roguelike.domain.effect.VisualEffect

class BattleFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var openSkillButton: Button
    private lateinit var battleMessage: TextView
    private lateinit var enemyHealthBar: ProgressBar
    private lateinit var attackButton: Button
    private lateinit var defenseButton: Button
    private lateinit var nextButton: Button
    private lateinit var enemyName: TextView
    private lateinit var enemySprite: ImageView
    private lateinit var monsterHealthText: TextView

    //    PLAYER
    private lateinit var playerHealthBar: ProgressBar
    private lateinit var playerHealthText: TextView
    private lateinit var playerManaBar: ProgressBar
    private lateinit var playerManaText: TextView
    private lateinit var playerStatsContainer: LinearLayout

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
        openSkillButton = view.findViewById(R.id.openSkillButton)
        battleMessage = view.findViewById(R.id.battleMessage)
        enemyHealthBar = view.findViewById(R.id.enemyHealthbar)
        monsterHealthText = view.findViewById(R.id.monsterHealthText)
        attackButton = view.findViewById(R.id.attackButton)
        defenseButton = view.findViewById(R.id.defenseButton)
        enemyName = view.findViewById(R.id.enemyName)
        nextButton = view.findViewById(R.id.nextBtn)

        playerHealthBar = view.findViewById(R.id.playerHealthbar)
        playerHealthText = view.findViewById(R.id.playerHealthText)
        playerManaBar = view.findViewById(R.id.playerManaBar)
        playerManaText = view.findViewById(R.id.playerManaText)
        playerStatsContainer = view.findViewById(R.id.playerStatsContainer)

        playerHealthText.text = "HP"
        playerManaText.text = "MP"

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
            SfxManager.play("defence")
        }
        openSkillButton.setOnClickListener {
                showSkillDialog()
            SfxManager.play("button")
        }
        nextButton.setOnClickListener {
            gameViewModel.performMonsterAction()
            turnManager.switchTurn()
            setActionButtonsVisible(true)
            setNextButtonVisible(false)
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

                monsterHealthText.text = "${monster.health}/${monster.maxHealth}"

                if (monster.health <= 0) {
                    showWinPopup(monster.name)
                }
            }
        }

        gameViewModel.battleMessage.observe(viewLifecycleOwner) { message ->
            battleMessage.text = message
        }
        gameViewModel.player.observe(viewLifecycleOwner){player->
            playerManaText.text = "${player.mana}/${player.maxMana}"
            playerHealthText.text = "${player.health}/${player.maxHealth}"
        }
    }

    private fun setActionButtonsVisible(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        attackButton.visibility = visibility
        defenseButton.visibility = visibility
        openSkillButton.visibility = visibility
        playerStatsContainer.visibility = visibility
    }

    private fun setNextButtonVisible(visible: Boolean) {
        nextButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showWinPopup(monsterName: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_victory, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val dialog = builder.create()

        dialogView.findViewById<TextView>(R.id.victoryMessage).text =
            "You defeated $monsterName!"

        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
            mainViewModel.navigateTo(Screen.Gacha)
        }
        dialog.setCanceledOnTouchOutside(false)
        // Hilangkan background putih default dialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showSkillDialog() {
        val player = gameViewModel.player.value ?: return
        val dialogView = layoutInflater.inflate(R.layout.dialog_skills, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val dialog = builder.create()

        val skillsContainer = dialogView.findViewById<LinearLayout>(R.id.skillsListContainer)

        // Tambahkan tombol untuk setiap skill
        for (skill in player.skills) {
            val btn = Button(requireContext()).apply {
                text = skill.name
                setOnClickListener {
                    val currentPlayer = gameViewModel.player.value ?: return@setOnClickListener
                    val manaRemain = currentPlayer.mana
                    if (manaRemain >= skill.manaCost) {
                        gameViewModel.performPlayerAction(PlayerAction.SKILL, skill)

                        // Tutup dialog
                        dialog.dismiss()

                        setActionButtonsVisible(false)
                        setNextButtonVisible(true)
                        SfxManager.play(skill.name)

                        if (skill.name.lowercase() != "heal") {
                            enemySprite?.let { sprite ->
                                VisualEffect.play(skill.name, sprite)
                            }
                        }
                    } else {
                        battleMessage?.text = "Didn't have enough mana"
                        SfxManager.play("decline")
                    }
                }
            }
            skillsContainer.addView(btn)
        }

        // Tombol Cancel di bawah daftar skill
        dialogView.findViewById<Button>(R.id.btnCloseSkillDialog).setOnClickListener {
            SfxManager.play("button")
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)

        // Hilangkan background putih default
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}

