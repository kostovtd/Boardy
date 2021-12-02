package com.kostovtd.herorealms.presenters

import android.util.Base64
import com.google.gson.Gson
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.boardy.util.Constants.FIRESTORE_VALUE_SEPARATOR
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.generateQRCodeBitmap
import com.kostovtd.herorealms.views.fragments.SetUpHeroRealmsPlayersView
import kotlinx.coroutines.launch

class SetUpHeroRealmsPlayersPresenter : BaseGamePresenter<SetUpHeroRealmsPlayersView>(),
    IGameSessionRepository {


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


    fun startHeroRealmsGameSession() {
        view?.let { view ->
            scopeIO.launch {
                scopeMainThread.launch {
                    view.showLoading()
                    view.disableAllViews()
                }

                val gameSessionPoints = HashMap<String, Int>()
                gameSessionFirestore?.teams?.forEach { team ->
                    val id = team.split(FIRESTORE_VALUE_SEPARATOR)[0]
                    gameSessionPoints[id] = 50
                }

                val responseStartGameSession = startGameSession()
                val isGameSessionStartedSuccessfully = handleResponse(responseStartGameSession)

                if (isGameSessionStartedSuccessfully) {
                    view.startHeroRealmsGameFragmentAsAdmin()
                } else {
                    handleError(responseStartGameSession.error)
                }
            }
        }
    }


    fun getGameSessionByIdAndAddCurrentUserToGameSession() {
        view?.let { view ->
            userRepository.getCurrentUser()?.uid?.let { userId ->
                userRepository.getCurrentUser()?.email?.let { userEmail ->
                    boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                        view.disableAllViews()
                        view.showLoading()

                        scopeIO.launch {
                            val getGameSessionByIdResponse = getGameSessionById(gameSessionId)
                            val isGameSessionFetched = handleResponse(getGameSessionByIdResponse)

                            if (isGameSessionFetched) {
                                val updateGameSessionResponse =
                                    addPlayerToGameSession(userId, userEmail, 50)
                                val isGameSessionUpdated = handleResponse(updateGameSessionResponse)

                                if (isGameSessionUpdated) {
                                    scopeMainThread.launch {
                                        view.enableAllViews()
                                        view.hideLoading()
                                    }
                                } else {
                                    handleError(updateGameSessionResponse.error)
                                }
                            } else {
                                handleError(getGameSessionByIdResponse.error)
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let {
            this.gameSessionFirestore = gameSessionFirestore

            it.showPlayers(
                ArrayList(gameSessionFirestore.players),
                gameSessionFirestore.adminId
            )

            when (playerType) {
                PlayerType.ADMIN -> {
                    if (gameSessionFirestore.players.size == 2) {
                        it.enableStartGame()
                    } else {

                    }
                }
                PlayerType.PLAYER -> {
                    gameSessionFirestore.startTime?.let { startTime ->
                        gameSessionFirestore.endTime?.let { endTime ->
                            if (startTime > endTime) {
                                it.startHeroRealmsGameFragmentAsPlayer()
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super<BaseGamePresenter>.onGameSessionFirestoreUpdateError(errorType)
    }
}