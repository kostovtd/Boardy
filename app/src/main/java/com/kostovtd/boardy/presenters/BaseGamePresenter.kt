package com.kostovtd.boardy.presenters

import com.google.firebase.Timestamp
import com.kostovtd.boardy.data.models.*
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.Resource
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var gameSessionDatabase: GameSessionDatabase? = null
    var boardGame: BoardGame? = null
    var playerType: PlayerType? = null
    var boardGameGameSession: BoardGameGameSession? = null


    protected suspend fun startGameSession(): Resource<BaseFirebaseResponse> {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                gameSessionFirestore?.startTime = Timestamp.now()
                gameSessionDatabase?.active = true

                return gameSessionRepository.updateGameSession(
                    gameSessionId,
                    gameSessionFirestore,
                    gameSessionDatabase
                )
            }
        } ?: run {
            return Resource(ResourceStatus.ERROR, null)
        }
    }


    protected suspend fun endGameSession(): Resource<BaseFirebaseResponse> {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                gameSessionFirestore?.endTime = Timestamp.now()
                gameSessionDatabase?.active = false

                return gameSessionRepository.updateGameSession(
                    gameSessionId,
                    gameSessionFirestore,
                    gameSessionDatabase
                )
            }
        } ?: run {
            return Resource(ResourceStatus.ERROR, null)
        }
    }


    protected fun subscribeGameSession(gameSessionId: String) {
        view?.let {
            subscribeGameSessionFirestore(gameSessionId)
            subscribeGameSessionDatabase(gameSessionId)
        }
    }


    protected fun unsubscribeGameSession() {
        view?.let {
            unsubscribeGameSessionFirebase()
            gameSessionDatabase?.id?.let { unsubscribeGameSessionDatabase(it) }
        }
    }


    private fun unsubscribeGameSessionDatabase(gameSessionId: String) {
        view?.let {
            gameSessionRepository.unsubscribeGameSessionDatabase(gameSessionId)
        }
    }


    private fun unsubscribeGameSessionFirebase() {
        view?.let {
            gameSessionRepository.unsubscribeGameSessionFirestore()
        }
    }


    private fun subscribeGameSessionFirestore(gameSessionId: String) {
        view?.let {
            gameSessionRepository.subscribeGameSessionFirestore(gameSessionId, this)
        }
    }


    private fun subscribeGameSessionDatabase(gameSessionId: String) {
        view?.let {
            gameSessionRepository.subscribeGameSessionDatabase(gameSessionId, this)
        }
    }
}