package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.herorealms.views.fragments.HeroRealmsGameView
import kotlinx.coroutines.launch

class HeroRealmsGamePresenter : BaseGamePresenter<HeroRealmsGameView>(), IGameSessionRepository {

    fun subscribeToGameSession() {
        view?.let {
            subscribeGameSession()
        }
    }

    fun unsubscribeFromGameSession() {
        view?.let {
            unsubscribeGameSession()
        }
    }


    fun changePlayerPoints(points: Int) {
        view?.let {
            scopeIO.launch {
                changePoints(points)
            }
        }
    }


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let { view ->
            view.updateGameSessionInfo(gameSessionFirestore)

            gameSessionFirestore.endTime?.let { endTime ->
                gameSessionFirestore.startTime?.let { startTime ->
                    if (endTime > startTime) {
                        unsubscribeGameSession()
                        view.finishActivity()
                    }
                }
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
//        super.onGameSessionFirestoreUpdateError(errorType)
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.let { view ->
            view.updatePoints(gameSessionDatabase.points)
            val gameEnded = gameSessionDatabase.points.containsValue(0)

            if (gameEnded && playerType == PlayerType.ADMIN) {
                scopeIO.launch {
                    val responseEndGameSession = endGameSession()
                    val isGameSessionEnded = handleResponse(responseEndGameSession)
                    if (!isGameSessionEnded) {
                        handleError(responseEndGameSession.error)
                    }
                }
            }
        }
    }


    override fun onGameSessionDatabaseUpdateError(errorType: ErrorType) {
//        super.onGameSessionDatabaseUpdateError(errorType)
    }
}