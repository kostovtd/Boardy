package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.models.*
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.Resource
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse
import com.kostovtd.boardy.web.responses.GameSessionByIdResult
import java.util.*

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var gameSessionDatabase: GameSessionDatabase? = null
    var boardGame: BoardGame? = null
    var playerType: PlayerType? = null
    var boardGameGameSession: BoardGameGameSession? = null


    suspend fun startGameSession(): Resource<BaseFirebaseResponse> {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                gameSessionFirestore?.startTime = Date().time
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


    suspend fun getGameSessionById(gameSessionId: String): Resource<GameSessionByIdResult> {
        view?.let {
            val response = gameSessionRepository.getGameSessionById(gameSessionId)
            val isGameSessionFetched = handleResponse(response)

            if (isGameSessionFetched) {
                gameSessionFirestore = response.data?.data?.gameSession
                gameSessionDatabase = response.data?.data?.realTimeGameSession
            }

            return response
        } ?: run {
            return Resource(ResourceStatus.ERROR, null)
        }
    }


    suspend fun endGameSession(): Resource<BaseFirebaseResponse> {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                gameSessionFirestore?.endTime = Date().time
                gameSessionFirestore?.status = GameSessionStatus.FINISHED
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


    suspend fun suspendGameSession(): Resource<BaseFirebaseResponse> {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                gameSessionFirestore?.status = GameSessionStatus.SUSPENDED
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


    fun subscribeGameSession() {
        view?.let {
            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                subscribeGameSessionFirestore(gameSessionId)
                subscribeGameSessionDatabase(gameSessionId)
            }
        }
    }


    fun unsubscribeGameSession() {
        view?.let {
            unsubscribeGameSessionFirebase()
            gameSessionDatabase?.id?.let { unsubscribeGameSessionDatabase(it) }
        }
    }


    suspend fun addPlayerToGameSession(
        userId: String,
        userEmail: String,
        points: Int,
        team: String = userId + Constants.FIRESTORE_VALUE_SEPARATOR + userEmail
    ): Resource<BaseFirebaseResponse> {
        view?.let {
            val playerEntryFirestore = userId + Constants.FIRESTORE_VALUE_SEPARATOR + userEmail

            if (gameSessionFirestore?.players?.contains(playerEntryFirestore) == false) {
                gameSessionFirestore?.players?.add(playerEntryFirestore)
            }

            if (gameSessionFirestore?.teams?.contains(team) == false) {
                gameSessionFirestore?.teams?.add(team)
            }

            gameSessionDatabase?.points?.set(userId, points)

            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
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


    suspend fun removePlayerFromGameSession(playerData: String): Resource<BaseFirebaseResponse> {
        view?.let {
            gameSessionFirestore?.players?.remove(playerData)
            gameSessionFirestore?.teams?.remove(playerData)
            gameSessionDatabase?.points?.remove(playerData.split(Constants.FIRESTORE_VALUE_SEPARATOR)[0])

            boardGameGameSession?.gameSessionId?.let { gameSessionId ->
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

    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        this.gameSessionFirestore = gameSessionFirestore
    }

    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super.onGameSessionFirestoreUpdateError(errorType)
    }

    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        this.gameSessionDatabase = gameSessionDatabase
    }

    override fun onGameSessionDatabaseUpdateError(errorType: ErrorType) {
        super.onGameSessionDatabaseUpdateError(errorType)
    }
}