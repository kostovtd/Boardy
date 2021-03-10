package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.views.activities.BaseView
import kotlinx.coroutines.launch

open class BaseGamePresenter<T> : BasePresenter<T>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null

    fun createGameSession(gameSessionFirestore: GameSessionFirestore) {
        view?.let { view ->
            scopeIO.launch {
                this@BaseGamePresenter.gameSessionFirestore = gameSessionFirestore

                scopeMainThread.launch {
                    (view as BaseView).showLoading()
                    (view as BaseView).disableAllViews()
                }

                val responseFirestore = gameSessionRepository.createGameSessionFirestore(gameSessionFirestore)
                val isGameSessionFirestoreCreated = handleResponse(responseFirestore)

                if (isGameSessionFirestoreCreated) {
                    responseFirestore.data?.let { data ->
                        this@BaseGamePresenter.gameSessionFirestore = data
                        val responseDatabase =
                            gameSessionRepository.createGameSessionDatabase(data)
                        val isGameSessionDatabaseCreated = handleResponse(responseDatabase)
                        if (isGameSessionDatabaseCreated) {
                            scopeMainThread.launch {
                                (view as BaseView).hideLoading()
                                (view as BaseView).enableAllViews()
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


    fun findGameSessionAndSubscribe(gameSessionId: String) {
        view?.let {
            gameSessionRepository.findAndListenGameSessionDatabase(gameSessionId, this)
        }
    }


    fun unsubscribeGameSession(gameSessionId: String) {
        view?.let {
            gameSessionRepository.stopListenGameSessionDatabase(gameSessionId)
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        (view as BaseView).handleGameSessionChanges(gameSessionDatabase)
    }
}