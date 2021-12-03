package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.data.repositories.BoardGamesRepository
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.util.DynamicModuleListener
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.startBoardGameModuleAs
import com.kostovtd.boardy.views.activities.MainView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 21.11.20.
 */
class MainPresenter : BasePresenter<MainView>(), DynamicModuleListener {

    private val boardGamesRepository = BoardGamesRepository()
    private val gameSessionRepository = GameSessionRepository()
//    private val dynamicModuleHandler = DynamicModuleHandler(this)
    private var gameSessionFirestore: GameSessionFirestore? = null
    var boardGameGameSession: BoardGameGameSession? = null
    var boardGame: BoardGame? = null


    fun signOut() {
        view?.let {
            userRepository.signOut()
            it.goToSignInActivity()
            it.finishActivity()
        }
    }


    fun findBoardGames() {
        view?.let { view ->
            view.showLoading()

            scopeIO.launch {
                val findBoardGamesResponse = boardGamesRepository.findGamesByName("Hero")
                when(findBoardGamesResponse.status) {
                    ResourceStatus.SUCCESS -> {
                        val boardgamesResult = findBoardGamesResponse.data
                        boardgamesResult?.let { boardgames ->
                            if(boardgames.isNotEmpty()) {
                                scopeMainThread.launch {
                                    view.hideLoading()
                                }

                                boardGame = boardgames.firstOrNull { boardgame ->
                                    boardgame.moduleName == "HeroRealms"
                                }
                                boardGame?.let {

                                    boardGameGameSession = BoardGameGameSession(null, it.id,
                                    it.packageName, it.moduleName, it.activityName)

                                    scopeMainThread.launch {
                                        view.enableDownloadGame()
                                    }
                                }
                            } else {
                                view.showError(ErrorType.UNKNOWN)
                            }
                        } ?: run  {
                            view.showError(ErrorType.UNKNOWN)
                        }
                    }
                    ResourceStatus.ERROR -> {
                        view.showError(ErrorType.UNKNOWN)
                    }
                }
            }
        }
    }

    
    fun startGame() {
        view?.let { view ->
            boardGameGameSession?.let { boardGameGameSession ->
                view.getViewContext()?.let { it -> startBoardGameModuleAs(PlayerType.ADMIN, it, boardGameGameSession) }
            }
        }
    }


    override fun onDynamicModuleDownloading() {
        view?.let {
            it.showLoading()
            it.showError(ErrorType.EMPTY_CONFIRM_PASSWORD)
        }
    }

    override fun onDynamicModuleInstalled() {
        view?.let {
            it.showError(ErrorType.EMPTY_EMAIL)
            it.enableStartGame()
            it.hideLoading()
        }
    }

    override fun onDynamicModuleError() {
        view?.let {
            it.hideLoading()
            it.disableAllViews()
        }
    }
}