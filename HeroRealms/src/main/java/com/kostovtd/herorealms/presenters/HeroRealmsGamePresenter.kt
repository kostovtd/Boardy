package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.views.activities.BaseView
import com.kostovtd.herorealms.views.fragments.HeroRealmsGameView
import kotlinx.coroutines.launch

class HeroRealmsGamePresenter: BaseGamePresenter<HeroRealmsGameView>(), IGameSessionRepository {

    var gameSessionId: String? = null

    private val userRepository = UserRepository()


    fun subscribeToGameSession() {
        view?.let {
            gameSessionId?.let { gameSessionId ->
                subscribeGameSession(gameSessionId)
            }
        }
    }

    fun unsubscribeFromGameSession() {
        view?.let {
            unsubscribeGameSession()
        }
    }


    fun getCurrentUserId(): String? = userRepository.getCurrentUser()?.uid


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let { view ->
            view.updateGameSessionInfo(gameSessionFirestore)

            if(gameSessionFirestore.endTime?.toDate()?.after(gameSessionFirestore.startTime?.toDate()) == true) {
//                gameSessionRepository.unsubscribeGameSessionFirestore()
                view.finishActivity()
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
//        super.onGameSessionFirestoreUpdateError(errorType)
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.let { view ->
            view.updatePoints(gameSessionDatabase.points)

            if(playerType == PlayerType.ADMIN) {
                val gameEnded = gameSessionDatabase.points.containsValue(0)
                if(gameEnded) {
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
        }
    }

    override fun onGameSessionDatabaseUpdateError(errorType: ErrorType) {
//        super.onGameSessionDatabaseUpdateError(errorType)
    }
}