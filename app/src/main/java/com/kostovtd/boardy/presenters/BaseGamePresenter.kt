package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.views.activities.BaseView
import kotlinx.coroutines.launch
import java.util.*

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var gameSessionDatabase: GameSessionDatabase? = null

    protected fun createGameSession(gameSessionFirestore: GameSessionFirestore, gameSessionDatabase: GameSessionDatabase) {
        view?.let { view ->
            scopeIO.launch {
                scopeMainThread.launch {
                    (view as BaseView).showLoading()
                    (view as BaseView).disableAllViews()
                }

                val responseFirestore =
                    gameSessionRepository.createGameSessionFirestore(gameSessionFirestore)
                val isGameSessionFirestoreCreated = handleResponse(responseFirestore)

                if (isGameSessionFirestoreCreated) {
                    responseFirestore.data?.let { dataFirestore ->
                        this@BaseGamePresenter.gameSessionFirestore = dataFirestore
                        gameSessionDatabase.id = dataFirestore.id

                        val responseDatabase =
                            gameSessionRepository.createGameSessionDatabase(gameSessionDatabase)
                        val isGameSessionDatabaseCreated = handleResponse(responseDatabase)

                        if (isGameSessionDatabaseCreated) {
                            responseDatabase.data?.let { data ->
                                this@BaseGamePresenter.gameSessionDatabase = data

                                scopeMainThread.launch {
                                    (view as BaseView).hideLoading()
                                    (view as BaseView).enableAllViews()
                                }
                            } ?: run {
                                handleError(ErrorType.UNKNOWN)
                            }
                        } else {
                            gameSessionRepository.deleteGameSessionFirestore(gameSessionFirestore.id)
                            handleError(responseDatabase.error)
                        }
                    } ?: run {
                        handleError(ErrorType.UNKNOWN)
                    }
                } else {
                    handleError(responseFirestore.error)
                }
            }
        }
    }


    protected fun startGameSession() {
        view?.let {
            gameSessionFirestore?.startTime = Date()
            gameSessionDatabase?.active = true

            gameSessionFirestore?.let { it1 -> updateGameSession(it1, gameSessionDatabase) }
        }
    }


    protected fun endGameSession() {
        view?.let {
            gameSessionFirestore?.endTime = Date()
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


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        (view as BaseView).handleGameSessionChanges(gameSessionDatabase)
    }
}