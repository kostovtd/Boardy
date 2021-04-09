package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.herorealms.views.activities.HeroRealmsView

/**
 * Created by tosheto on 16.02.21.
 */
class HeroRealmsPresenter : BaseGamePresenter<HeroRealmsView>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    private val userRepository = UserRepository()
    var playerType: PlayerType? = null
    var boardGameGameSession: BoardGameGameSession? = null


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