package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.presenters.BasePresenter
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.herorealms.views.fragments.HeroRealmsGameView
import kotlinx.coroutines.launch
import java.util.*

class HeroRealmsGamePresenter: BasePresenter<HeroRealmsGameView>(), IGameSessionRepository {

    var playerType: PlayerType? = null
    var gameSessionId: String? = null

    private val gameSessionRepository = GameSessionRepository()
    private val userRepository = UserRepository()


    fun subscribeToGameSession() {
        view?.let {
            gameSessionId?.let {
                gameSessionRepository.subscribeGameSessionFirestore(it, this)
                gameSessionRepository.subscribeGameSessionDatabase(it,this)
            }
        }
    }

    fun unsubscribeFromGameSession() {
        view?.let {
            gameSessionId?.let {
                gameSessionRepository.unsubscribeGameSessionFirestore()
                gameSessionRepository.unsubscribeGameSessionDatabase(it)
            }
        }
    }


    fun getCurrentUserId(): String? = userRepository.getCurrentUser()?.uid


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let { view ->
            view.updateGameSessionInfo(gameSessionFirestore)

            if(gameSessionFirestore.endTime?.toDate()?.after(gameSessionFirestore.startTime?.toDate()) == true) {
                gameSessionRepository.unsubscribeGameSessionFirestore()
                view.finishActivity()
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super.onGameSessionFirestoreUpdateError(errorType)
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.let { view ->
            view.updatePoints(gameSessionDatabase.points)

            if(playerType == PlayerType.ADMIN) {
                val gameEnded = gameSessionDatabase.points.containsValue(0)
                if(gameEnded) {
                    scopeIO.launch {
                        gameSessionRepository.updateGameSessionFirestoreField(
                            gameSessionId ?: "",
                            Constants.END_TIME_FIELD,
                            Date(),
                            false
                        )
                    }
                }
            }
        }
    }


    override fun onGameSessionDatabaseUpdateError(errorType: ErrorType) {
        super.onGameSessionDatabaseUpdateError(errorType)
    }
}