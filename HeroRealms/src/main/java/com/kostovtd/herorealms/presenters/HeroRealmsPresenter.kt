package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BasePresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.herorealms.views.activities.HeroRealmsView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 16.02.21.
 */
class HeroRealmsPresenter : BasePresenter<HeroRealmsView>(), IGameSessionRepository {

    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null

    fun createGameSession() {
        view?.let { view ->

            scopeIO.launch {
                gameSessionFirestore = GameSessionFirestore(
                    "AAA",
                    "BBB",
                    "CCC",
                    "DD/MM/YYYY",
                    arrayListOf("Tom", "Jerry"),
                    hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                    hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                    "dd/mm/yyyy",
                    arrayListOf("Jerry"),
                    50,
                )

                scopeMainThread.launch {
                    view.showLoading()
                    view.disableAllViews()
                }

                gameSessionFirestore?.let {
                    val responseFirestore = gameSessionRepository.createGameSessionFirestore(it)
                    val isGameSessionFirestoreCreated = handleResponse(responseFirestore)

                    if (isGameSessionFirestoreCreated) {
                        responseFirestore.data?.let { data ->
                            val responseDatabase =
                                gameSessionRepository.createGameSessionDatabase(data)
                            val isGameSessionDatabaseCreated = handleResponse(responseDatabase)
                            if (isGameSessionDatabaseCreated) {
                                scopeMainThread.launch {
                                    view.hideLoading()
                                    view.enableAllViews()
                                }
                            } else {
                                gameSessionRepository.deleteGameSessionFirestore(it.id)
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
    }


    fun findAndListen(gameSessionId: String) {
        view?.let {
            gameSessionRepository.findAndListenGameSessionDatabase(gameSessionId, this)
        }
    }


    fun stopListenGameSession() {
        view?.let {
            gameSessionRepository.stopListenGameSessionDatabase()
        }
    }


    override fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {
        view?.handleGameSessionChanges(gameSessionDatabase)
    }
}