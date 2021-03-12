package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.herorealms.views.activities.HeroRealmsView
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by tosheto on 16.02.21.
 */
class HeroRealmsPresenter : BaseGamePresenter<HeroRealmsView>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()

    fun createHeroRealmsGameSession() {
        view?.let {
            val gameSessionFirestore = GameSessionFirestore(
                "",
                "BBB",
                "CCC",
                Date(),
                arrayListOf("Tom", "Jerry"),
                hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                Date(),
                arrayListOf("Jerry"),
                50,
            )

            val gameSessionDatabase = GameSessionDatabase(points = HashMap())

            gameSessionFirestore.teams.forEach { team ->
                gameSessionDatabase.points[team.key] =
                    gameSessionFirestore.startingPoints
            }

            createGameSession(gameSessionFirestore, gameSessionDatabase)
        }
    }


    fun subscribeHeroRealmsGameSession(gameSessionId: String) {
        view?.let {
            subscribeGameSession(gameSessionId)
        }
    }


    fun unsubscribeHeroRealmsGameSession() {
        view?.let {
            unsubscribeGameSession()
        }
    }


    fun startHeroRealmsGameSession() {
        view?.let {
            startGameSession()
        }
    }


    fun endHeroRealmsGameSession() {
        view?.let {
            endGameSession()
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.let {
            this.gameSessionDatabase = gameSessionDatabase
            it.handleGameSessionChanges(gameSessionDatabase)
        }
    }
}