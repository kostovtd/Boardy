package com.kostovtd.boardy.presenters

import com.google.firebase.Timestamp
import com.kostovtd.boardy.data.models.*
import com.kostovtd.boardy.data.repositories.*
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse
import com.kostovtd.boardy.web.responses.GameSessionByIdResult

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    val userRepository = UserRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var gameSessionDatabase: GameSessionDatabase? = null
    var boardGame: BoardGame? = null
    var playerType: PlayerType? = null
    var boardGameGameSession: BoardGameGameSession? = null


    suspend fun startGameSession(): Resource<BaseFirebaseResponse> {
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
        points: Int
    ): Resource<BaseFirebaseResponse> {
        view?.let {
            val playerEntryFirestore = userId + Constants.FIRESTORE_VALUE_SEPARATOR + userEmail
            gameSessionFirestore?.players?.add(playerEntryFirestore)

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


    fun removePlayerFromGameSession() {

    }


    fun isUserInGameSession(
        userId: String,
        userEmail: String
    ): Boolean {
        val playerEntryFirestore = userId + Constants.FIRESTORE_VALUE_SEPARATOR + userEmail
        return gameSessionFirestore?.players?.contains(playerEntryFirestore) == true
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