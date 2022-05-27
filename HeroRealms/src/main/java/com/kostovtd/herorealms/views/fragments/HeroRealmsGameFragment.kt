package com.kostovtd.herorealms.views.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.presenters.HeroRealmsGamePresenter
import kotlinx.android.synthetic.main.fragment_hero_realms_game.*

class HeroRealmsGameFragment : Fragment(R.layout.fragment_hero_realms_game), HeroRealmsGameView {

    private val presenter = HeroRealmsGamePresenter()
    private lateinit var timer: CountDownTimer

    override fun getViewContext(): Context? = context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        arguments?.getSerializable(Constants.PLAYER_TYPE_KEY).let {
            presenter.playerType = it as PlayerType
        }

        arguments?.getSerializable(Constants.BOARD_GAME_GAME_SESSION_KEY).let {
            presenter.boardGameGameSession = it as BoardGameGameSession
        }

        timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                presenter.changePlayerPoints(yourPoints.text.toString().toInt())
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
    }


    override fun onResume() {
        super.onResume()
        presenter.subscribeToGameSession()
    }


    override fun onPause() {
        super.onPause()
        presenter.unsubscribeFromGameSession()
    }


    override fun updatePoints(pointsHashMap: HashMap<String, Int>) {
        var pointsStr: String
        for ((playerId, points) in pointsHashMap) {
            pointsStr = if (points > 0) {
                points.toString()
            } else {
                "0"
            }

            if (playerId == presenter.getCurrentUserId()) {
                yourPoints.text = pointsStr
            } else {
                opponentPoints.text = pointsStr
            }
        }
    }


    override fun updateGameSessionInfo(gameSessionFirestore: GameSessionFirestore) {

    }


    override fun finishActivity() {
        activity?.finish()
    }
}