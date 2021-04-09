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
import com.kostovtd.boardy.util.Constants.FIRESTORE_VALUE_SEPARATOR
import com.kostovtd.boardy.util.Constants.PLAYERS_FIELD
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


    fun addCurrentUserToFirestorePlayersListAndSubscribe() {
        view?.let { view ->
            view.disableAllViews()
            view.showLoading()

            scopeIO.launch {
                boardGameGameSession?.gameSessionId?.let { gameSessionId ->
                    userRepository.getCurrentUser()?.let { user ->
                        val playersArrEntry = user.uid + FIRESTORE_VALUE_SEPARATOR + user.email

                        val updateGameSessionFirestoreFieldResponse = gameSessionRepository.updateGameSessionFirestoreField(
                            gameSessionId,
                            PLAYERS_FIELD,
                            playersArrEntry,
                            true
                        )

                        val isFirestoreFieldUpdated = handleResponse(updateGameSessionFirestoreFieldResponse)

                        if(isFirestoreFieldUpdated) {
                            subscribeGameSessionFirestore()
                        }

                        scopeMainThread.launch {
                            view.enableAllViews()
                            view.hideLoading()
                        }
                    }
                }
            }
        }
    }


    fun subscribeGameSessionFirestore() {
        view?.let {
            boardGameGameSession?.gameSessionId?.let {
                gameSessionRepository.subscribeGameSessionFirestore(it, this)
            }
        }
    }


    override fun onGameSessionFirestoreUpdated(gameSessionFirestore: GameSessionFirestore) {
        view?.let {
            this.gameSessionFirestore = gameSessionFirestore
            it.showPlayers(ArrayList(gameSessionFirestore.players), gameSessionFirestore.adminId)
        }
    }


    override fun onGameSessionFirestoreUpdateError(errorType: ErrorType) {
        super.onGameSessionFirestoreUpdateError(errorType)
    }
}