package com.kostovtd.boardy.presenters

import com.google.firebase.Timestamp
import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.views.activities.BaseView
import kotlinx.coroutines.launch
import java.util.*

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var gameSessionDatabase: GameSessionDatabase? = null
    var boardGame: BoardGame? = null


    protected fun startGameSession() {
        view?.let {
            gameSessionFirestore?.startTime = Timestamp.now()
            gameSessionDatabase?.active = true

            gameSessionFirestore?.let { it1 -> updateGameSession(it1, gameSessionDatabase) }
        }
    }


    protected fun endGameSession() {
        view?.let {
            gameSessionFirestore?.endTime = Timestamp.now()
            gameSessionDatabase?.active = false

            gameSessionFirestore?.let { it1 -> updateGameSession(it1, gameSessionDatabase) }
        }
    }


    private fun updateGameSession(
        gameSessionFirestore: GameSessionFirestore,
        gameSessionDatabase: GameSessionDatabase?
    ) {
        view?.let {
            scopeIO.launch {
                this@BaseGamePresenter.gameSessionFirestore = gameSessionFirestore

                scopeMainThread.launch {
                    (view as BaseView).showLoading()
                    (view as BaseView).disableAllViews()
                }

                val responseFirestore =
                    gameSessionRepository.updateGameSessionFirestore(gameSessionFirestore)
                val isGameSessionFirestoreUpdated = handleResponse(responseFirestore)

                if (isGameSessionFirestoreUpdated) {
                    gameSessionDatabase?.let { gameSessionDatabase ->
                        this@BaseGamePresenter.gameSessionDatabase = gameSessionDatabase

                        val responseDatabase = gameSessionRepository.updateGameSessionDatabase(
                            gameSessionFirestore.id,
                            gameSessionDatabase
                        )
                        val isGameSessionDatabaseUpdate = handleResponse(responseDatabase)

                        if (isGameSessionDatabaseUpdate) {
                            scopeMainThread.launch {
                                (view as BaseView).hideLoading()
                                (view as BaseView).enableAllViews()
                            }
                        } else {
                            handleError(responseDatabase.error)
                        }
                    }
                } else {
                    handleError(responseFirestore.error)
                }
            }
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

    protected fun subscribeGameSessionFirestore(gameSessionId: String) {
        view?.let {
            gameSessionRepository.subscribeGameSessionFirestore(gameSessionId, this)
        }
    }


    protected fun subscribeGameSessionDatabase(gameSessionId: String) {
        view?.let {
            gameSessionRepository.subscribeGameSessionDatabase(gameSessionId, this)
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        (view as BaseView).handleGameSessionChanges(gameSessionDatabase)
    }
}