package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.boardy.views.activities.BaseView
import com.kostovtd.herorealms.views.activities.HeroRealmsView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 16.02.21.
 */
//TODO Remove the methods if they are not needed at all
class HeroRealmsPresenter : BaseGamePresenter<HeroRealmsView>(), IGameSessionRepository {

    fun startHeroRealmsGameSession() {
        view?.let {
            scopeIO.launch {
                val responseStartGameSession = startGameSession()

                val isGameSessionStarted = handleResponse(responseStartGameSession)

                if (isGameSessionStarted) {
                    scopeMainThread.launch {
                        (view as BaseView).hideLoading()
                        (view as BaseView).enableAllViews()
                    }
                } else {
                    handleError(responseStartGameSession.error)
                }
            }
        }
    }


    fun endHeroRealmsGameSession() {
        view?.let {
            scopeIO.launch {
                val responseEndGameSession = endGameSession()

                val isGameSessionEnded = handleResponse(responseEndGameSession)

                if (isGameSessionEnded) {
                    scopeMainThread.launch {
                        (view as BaseView).hideLoading()
                        (view as BaseView).enableAllViews()
                    }
                } else {
                    handleError(responseEndGameSession.error)
                }
            }
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.let {
            this.gameSessionDatabase = gameSessionDatabase
            it.handleGameSessionChanges(gameSessionDatabase)
        }
    }
}