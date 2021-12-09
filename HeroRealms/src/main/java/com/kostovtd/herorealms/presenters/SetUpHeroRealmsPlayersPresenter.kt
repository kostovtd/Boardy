package com.kostovtd.herorealms.presenters

import android.util.Base64
import com.google.gson.Gson
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.GameSessionStatus
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.IGameSessionRepository
import com.kostovtd.boardy.presenters.BaseGamePresenter
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.generateQRCodeBitmap
import com.kostovtd.herorealms.views.fragments.SetUpHeroRealmsPlayersView
import kotlinx.coroutines.launch

class SetUpHeroRealmsPlayersPresenter : BaseGamePresenter<SetUpHeroRealmsPlayersView>(),
    IGameSessionRepository {

    var wasBackClicked: Boolean = false


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

                val responseStartGameSession = startGameSession()
                val isGameSessionStartedSuccessfully = handleResponse(responseStartGameSession)

                if (isGameSessionStartedSuccessfully) {
                    scopeMainThread.launch {
                        view.hideLoading()
                        view.enableAllViews()
                    }
                } else {
                    handleError(responseStartGameSession.error)
                }
            }
        }
    }


    fun suspendHeroRealmsGameSession() {
        view?.let { view ->
            scopeIO.launch {
                scopeMainThread.launch {
                    view.showLoading()
                    view.disableAllViews()

                    val responseSuspendGameSession = suspendGameSession()
                    val isGameSessionSuspendedSuccessfully = handleResponse(responseSuspendGameSession)

                    if (isGameSessionSuspendedSuccessfully) {
                        scopeMainThread.launch {
                            view.hideLoading()
                        }
                    } else {
                        handleError(responseSuspendGameSession.error)
                    }
                }
            }
        }
    }


    fun getGameSessionByIdAndAddCurrentUserToGameSession() {
        view?.let { view ->
            getCurrentUserId()?.let { userId ->
                getCurrentUserEmail()?.let { userEmail ->
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
                                    subscribeGameSession()

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


    fun removePlayerFromHeroRealmsGameSession(playerData: String?) {
        view?.let { view ->
            playerData?.let { playerData ->
                view.disableAllViews()
                view.showLoading()

                scopeIO.launch {
                    val removePlayerResponse = removePlayerFromGameSession(playerData)
                    val isPlayerRemoved = handleResponse(removePlayerResponse)

                    if(isPlayerRemoved) {
                        scopeMainThread.launch {
                            view.enableAllViews()
                            view.hideLoading()

                            if(gameSessionFirestore?.players?.size ?: 0 < 2) {
                                view.disableStartGame()
                            }
                        }
                    } else {
                        handleError(removePlayerResponse.error)
                    }
                }
            }
        }
    }


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        super<BaseGamePresenter>.onGameSessionFirestoreUpdated(gameSessionFirestore)

        view?.let {
            if(gameSessionFirestore.status == GameSessionStatus.SUSPENDED) {
                it.onGameSessionSuspended()
                return
            }

            val userData =
                getCurrentUserId() + Constants.FIRESTORE_VALUE_SEPARATOR + getCurrentUserEmail()

            if (!gameSessionFirestore.players.contains(userData) && playerType == PlayerType.PLAYER) {
                if(!wasBackClicked) {
                    it.onCurrentPlayerRemovedFromGameSession()
                } else {
                    it.finishActivity()
                }
                wasBackClicked = false
                return
            }

            it.showPlayers(
                ArrayList(gameSessionFirestore.players),
                gameSessionFirestore.adminId,
                getCurrentUserId() ?: ""
            )

            if(playerType == PlayerType.ADMIN) {
                if (gameSessionFirestore.players.size >= 2) {
                    it.enableStartGame()
                } else {
                    it.disableStartGame()
                }
            }

            gameSessionFirestore.startTime?.let { startTime ->
                gameSessionFirestore.endTime?.let { endTime ->
                    if (startTime > endTime) {
                        if(playerType == PlayerType.ADMIN) {
                            it.startHeroRealmsGameFragmentAsAdmin()
                        } else {
                            it.startHeroRealmsGameFragmentAsPlayer()
                        }
                    }
                }
            }
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super<BaseGamePresenter>.onGameSessionFirestoreUpdateError(errorType)
    }
}