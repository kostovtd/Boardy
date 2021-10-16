package com.kostovtd.herorealms.presenters

import com.google.firebase.Timestamp
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.presenters.BasePresenter
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.views.fragments.SetUpHeroRealmsModeView
import kotlinx.coroutines.launch

class SetUpHeroRealmsModePresenter : BasePresenter<SetUpHeroRealmsModeView>() {

    private val userRepository = UserRepository()
    private val gameSessionRepository = GameSessionRepository()
    var boardGameGameSession: BoardGameGameSession? = null


    fun createHeroRealmsGameSession() {
        view?.let { view ->
            val adminId = userRepository.getCurrentUser()?.uid ?: ""
            val endTime = Timestamp.now()
            val losers = ArrayList<String>()

            val playerArrEntry = (userRepository.getCurrentUser()?.uid ?: "") +
                    Constants.FIRESTORE_VALUE_SEPARATOR + userRepository.getCurrentUser()?.email
            val players = arrayListOf(playerArrEntry)

            val teamArrEntry = (userRepository.getCurrentUser()?.uid ?: "") +
                    Constants.FIRESTORE_VALUE_SEPARATOR + userRepository.getCurrentUser()?.email
            val teams = arrayListOf(teamArrEntry)

            val startTime = Timestamp.now()
            val winners = ArrayList<String>()
            val startingPoints = 50

            val gameSessionFirestoreObj = GameSessionFirestore(
                "", adminId, boardGameGameSession?.boardGameId ?: "", startTime, endTime,
                startingPoints, players, teams, winners, losers
            )

            view.showLoading()
            view.disableAllViews()

            scopeIO.launch {
                val responseFirestore =
                    gameSessionRepository.createGameSession(gameSessionFirestoreObj)
                val isGameSessionFirestoreCreated = handleResponse(responseFirestore)

                if (isGameSessionFirestoreCreated) {
                    boardGameGameSession?.gameSessionId = responseFirestore.data?.gameSessionId

                    scopeMainThread.launch {
                        view.hideLoading()
                        view.enableAllViews()
                        boardGameGameSession?.let {
                            view.startSetUpHeroRealmsPlayersFragmentAsAdmin(
                                it
                            )
                        }
                    }

                } else {
                    handleError(responseFirestore.error)
                }
            }
        }
    }
}