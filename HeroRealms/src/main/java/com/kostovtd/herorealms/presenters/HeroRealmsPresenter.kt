package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.herorealms.views.activities.HeroRealmsView

/**
 * Created by tosheto on 16.02.21.
 */
class HeroRealmsPresenter : BaseGamePresenter<HeroRealmsView>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()

    fun createHeroRealmsGameSession() {
        view?.let {
            val gameSession = GameSessionFirestore(
                "AAA",
                "BBB",
                "CCC",
                "DD/MM/YYYY",
                arrayListOf("Tom", "Jerry"),
                hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                "dd/mm/yyyy",
                arrayListOf("Jerry"),
                50,
            )
            createGameSession(gameSession)
        }
    }


    fun findHeroRealmsGameSessionAndListen(gameSessionId: String) {
        view?.let {
            findGameSessionAndSubscribe(gameSessionId)
        }
    }


    fun unsubscribeHeroRealmsGameSession(gameSessionId: String) {
        view?.let {
            unsubscribeGameSession(gameSessionId)
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.handleGameSessionChanges(gameSessionDatabase)
    }
}