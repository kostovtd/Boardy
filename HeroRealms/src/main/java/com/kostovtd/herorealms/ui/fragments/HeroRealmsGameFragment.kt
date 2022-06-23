package com.kostovtd.herorealms.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kostovtd.boardy.data.models.GameSessionStatus
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.ui.customViews.AnimatedConfirmationDialog
import com.kostovtd.boardy.ui.customViews.AnimatedInformationDialog
import com.kostovtd.boardy.ui.customViews.AnimationType
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.ui.viewModels.HeroRealmsViewModel
import kotlinx.android.synthetic.main.fragment_hero_realms_game.*

class HeroRealmsGameFragment : Fragment(R.layout.fragment_hero_realms_game) {

    private val viewModel: HeroRealmsViewModel by activityViewModels()
    private lateinit var timer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                viewModel.changePoints(yourPoints.text.toString().toInt())
            }
        }

        add.setOnClickListener {
            timer.cancel()
            timer.start()
            yourPoints.text =
                (yourPoints.text.toString().toInt() + points.text.toString().toInt()).toString()
        }

        subtract.setOnClickListener {
            timer.cancel()
            timer.start()
            val newPoints = yourPoints.text.toString().toInt() - points.text.toString().toInt()
            yourPoints.text = if (newPoints > 0) {
                newPoints.toString()
            } else {
                "0"
            }
        }

        setObservers()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val titleId = com.kostovtd.boardy.R.string.are_you_sure

                val descriptionId = if (viewModel.playerType.value == PlayerType.ADMIN) {
                    com.kostovtd.boardy.R.string.you_are_about_to_leave_game_session_admin
                } else {
                    com.kostovtd.boardy.R.string.you_are_about_to_leave_game_session_player
                }

                val positiveCallback = {
                    if (viewModel.playerType.value == PlayerType.ADMIN) {
                        viewModel.suspendGameSession()
                    } else {
                        viewModel.removeCurrentPlayerFromGameSession()
                    }
                }

                showConfirmationDialog(
                    getString(titleId),
                    getString(descriptionId), AnimationType.ATTENTION, positiveCallback
                )
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setObservers() {
        observeGameSession()
        observePlayers()
    }


    private fun observePlayers() {
        viewModel.points.observe(viewLifecycleOwner) {
            updatePoints(it)

            if (viewModel.playerType.value == PlayerType.ADMIN && viewModel.gameSessionStatus.value != GameSessionStatus.FINISHED &&
                viewModel.isGameFinished()
            ) {
                viewModel.endGameSession()
            }
        }
    }


    private fun observeGameSession() {
        viewModel.gameSessionStatus.observe(viewLifecycleOwner) {
            when (it) {
                GameSessionStatus.SUSPENDED -> {
                    onGameSessionSuspended()
                }
                GameSessionStatus.FINISHED -> {
                    onGameSessionFinished()
                }
                else -> {}
            }
        }
    }

    private fun updatePoints(pointsHashMap: HashMap<String, Int>) {
        var pointsStr: String
        for ((playerId, points) in pointsHashMap) {
            pointsStr = if (points > 0) {
                points.toString()
            } else {
                "0"
            }

            if (playerId == viewModel.currentPlayer?.uid) {
                yourPoints.text = pointsStr
            } else {
                opponentPoints.text = pointsStr
            }
        }
    }


    private fun onGameSessionSuspended() {
        when (viewModel.playerType.value) {
            PlayerType.ADMIN -> {
                showInformationDialog(
                    getString(com.kostovtd.boardy.R.string.yay),
                    getString(com.kostovtd.boardy.R.string.game_session_suspended_admin),
                    AnimationType.SUCCESS
                ) { activity?.finish() }
            }
            else -> {
                showInformationDialog(
                    getString(com.kostovtd.boardy.R.string.bad_news),
                    getString(com.kostovtd.boardy.R.string.game_session_suspended_player),
                    AnimationType.BAD_NEWS
                ) { activity?.finish() }
            }
        }
    }


    private fun onGameSessionFinished() {
        if(viewModel.isCurrentPlayerAWinner()) {
            showInformationDialog(
                getString(com.kostovtd.boardy.R.string.yay),
                getString(com.kostovtd.boardy.R.string.you_are_a_winner),
                AnimationType.SUCCESS
            ) { activity?.finish() }
        } else {
            showInformationDialog(
                getString(com.kostovtd.boardy.R.string.bad_news),
                getString(com.kostovtd.boardy.R.string.you_are_a_loser),
                AnimationType.BAD_NEWS
            ) { activity?.finish() }
        }
    }


    private fun showConfirmationDialog(
        title: String,
        description: String,
        animationType: AnimationType,
        positiveCallback: () -> Unit
    ) {
        activity?.let {
            AnimatedConfirmationDialog(
                it, title,
                description,
                positiveCallback,
                animationType,
                true
            ).show()
        }
    }


    private fun showInformationDialog(
        title: String,
        description: String,
        animationType: AnimationType,
        callback: () -> Unit
    ) {
        activity?.let {
            AnimatedInformationDialog(
                it, title,
                description,
                callback,
                animationType,
                true
            ).show()
        }
    }
}