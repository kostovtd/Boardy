package com.kostovtd.herorealms.presenters

import android.util.Base64
import com.google.gson.Gson
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.presenters.BasePresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.generateQRCodeBitmap
import com.kostovtd.herorealms.views.fragments.SetUpHeroRealmsPlayersView
import kotlinx.coroutines.launch

class SetUpHeroRealmsPlayersPresenter : BasePresenter<SetUpHeroRealmsPlayersView>(),
    IGameSessionRepository {

    private val userRepository = UserRepository()
    private val gameSessionRepository = GameSessionRepository()
    var gameSessionFirestore: GameSessionFirestore? = null
    var boardGameGameSession: BoardGameGameSession? = null
    var playerType: PlayerType? = null


    fun generateQRCode() {
        view?.let { view ->
            boardGameGameSession?.let { it ->
                scopeIO.launch {
                    val gameSessionQRJson = Gson().toJson(it)
                    val gameSessionQRJsonBase64 =
                        Base64.encodeToString(gameSessionQRJson.toByteArray(), Base64.DEFAULT)
                    val qrCode = generateQRCodeBitmap(gameSessionQRJsonBase64)

                    scopeMainThread.launch {
                        qrCode?.let { view.showQRcode(it) }
                    }
                }
            }
        }
    }

    fun startGameSession() {
        view?.let { view ->
//            scopeIO.launch {
//                scopeMainThread.launch {
//                    view.showLoading()
//                    view.disableAllViews()
//                }
//
//                val gameSessionPoints = HashMap<String, Int>()
//                gameSessionFirestore?.teams?.forEach { team ->
//                    val id = team.split(FIRESTORE_VALUE_SEPARATOR)[0]
//                    gameSessionPoints[id] = 50
//                }
//
//                val gameSessionDatabase = GameSessionDatabase(
//                    boardGameGameSession?.gameSessionId ?: "",
//                    true,
//                    gameSessionPoints
//                )
//
//                val responseDatabase =
//                    gameSessionRepository.createGameSessionDatabase(gameSessionDatabase)
//                val isGameSessionDatabaseCreated = handleResponse(responseDatabase)
//
//                if (isGameSessionDatabaseCreated) {
//                    val responseUpdateGameSessionFirestore = gameSessionRepository.updateGameSessionFirestoreField(
//                        boardGameGameSession?.gameSessionId ?: "",
//                        START_TIME_FIELD, Date(), false)
//                    val isGameSessionFirestoreUpdated = handleResponse(responseUpdateGameSessionFirestore)
//                    if(!isGameSessionFirestoreUpdated) {
//                        handleError(responseDatabase.error)
//                    }
//                } else {
//                    handleError(responseDatabase.error)
//                }
//            }
        }
    }

    fun addCurrentUserToFirestorePlayersListAndSubscribe() {
        view?.let { view ->
//            view.disableAllViews()
//            view.showLoading()
//
//            scopeIO.launch {
//                boardGameGameSession?.gameSessionId?.let { gameSessionId ->
//                    userRepository.getCurrentUser()?.let { user ->
//                        val playersArrEntry = user.uid + FIRESTORE_VALUE_SEPARATOR + user.email
//
//                        val updateGameSessionFirestorePlayersFieldResponse = gameSessionRepository.updateGameSessionFirestoreField(
//                            gameSessionId,
//                            PLAYERS_FIELD,
//                            playersArrEntry,
//                            true
//                        )
//
//                        val isFirestorePlayersFieldUpdated = handleResponse(updateGameSessionFirestorePlayersFieldResponse)
//
//                        if(isFirestorePlayersFieldUpdated) {
//                            val teamsArrEntry = user.uid + FIRESTORE_VALUE_SEPARATOR + user.email
//
//                            val updateGameSessionFirestoreTeamsFieldResponse = gameSessionRepository.updateGameSessionFirestoreField(
//                                gameSessionId,
//                                TEAMS_FIELD,
//                                teamsArrEntry,
//                                true
//                            )
//
//                            val isFirestoreTeamsFieldUpdated = handleResponse(updateGameSessionFirestoreTeamsFieldResponse)
//
//                            if(isFirestoreTeamsFieldUpdated) {
//                                subscribeGameSessionFirestore()
//                            }
//                        }
//
//                        scopeMainThread.launch {
//                            view.enableAllViews()
//                            view.hideLoading()
//                        }
//                    }
//                }
//            }
        }
    }


    fun subscribeGameSessionFirestore() {
        view?.let {
            boardGameGameSession?.gameSessionId?.let {
//                gameSessionRepository.subscribeGameSessionFirestore(it, this)
            }
        }
    }


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let {
            this.gameSessionFirestore = gameSessionFirestore
            when(playerType) {
                PlayerType.ADMIN -> {
                    if(gameSessionFirestore.startTime?.toDate()?.after(gameSessionFirestore.endTime?.toDate()) == true) {
                        gameSessionRepository.unsubscribeGameSessionFirestore()
                        it.startHeroRealmsGameFragmentAsAdmin()
                    } else {
                        if(gameSessionFirestore.players.size == 2) {
                            it.enableStartGame()
                        }
                        it.showPlayers(ArrayList(gameSessionFirestore.players), gameSessionFirestore.adminId)
                    }
                }
                PlayerType.PLAYER -> {
                    if(gameSessionFirestore.startTime?.toDate()?.after(gameSessionFirestore.endTime?.toDate()) == true) {
                        gameSessionRepository.unsubscribeGameSessionFirestore()
                        it.startHeroRealmsGameFragmentAsAdmin()
                    } else {
                        it.showPlayers(ArrayList(gameSessionFirestore.players), gameSessionFirestore.adminId)
                    }
                }
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super.onGameSessionFirestoreUpdateError(errorType)
    }
}