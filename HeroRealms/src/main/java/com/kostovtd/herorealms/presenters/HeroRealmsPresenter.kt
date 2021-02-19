package com.kostovtd.herorealms.presenters

import com.kostovtd.boardy.data.models.GameSession
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.presenters.BasePresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.herorealms.views.activities.HeroRealmsView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 16.02.21.
 */
class HeroRealmsPresenter : BasePresenter<HeroRealmsView>() {

    private val gameSessionRepository = GameSessionRepository()
    private var gameSession: GameSession? = null

    fun createGameSession() {
        view?.let { view ->

            scopeIO.launch {
                gameSession = GameSession(
                    "AAA",
                    "BBB",
                    "CCC",
                    "DD/MM/YYYY",
                    arrayListOf("Tom", "Jerry"),
                    hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                    hashMapOf(Pair("ZZZ", "Tom"), Pair("XXX", "Jerry")),
                    "dd/mm/yyyy",
                    arrayListOf("Jerry"),
                    50
                )

                scopeMainThread.launch {
                    view.showLoading()
                    view.disableAllViews()
                }

                gameSession?.let {
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
}