package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.data.repositories.BoardGamesRepository
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.util.DynamicModuleHandler
import com.kostovtd.boardy.util.DynamicModuleListener
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.views.activities.MainView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 21.11.20.
 */
class MainPresenter : BasePresenter<MainView>(), DynamicModuleListener {

    private val userRepository = UserRepository()
    private val boardGamesRepository = BoardGamesRepository()
    private val dynamicModuleHandler = DynamicModuleHandler(this)
    private var boardGame: BoardGame? = null

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


    fun downloadAndInstallGame() {
        view?.let { view ->
            boardGame?.let {
                if(!dynamicModuleHandler.isModuleInstalled(view.getContext(), it.moduleName)) {
                    dynamicModuleHandler.downloadAndInstallModule(view.getContext(), it.moduleName)
                } else {
                    boardGame?.let {
                        dynamicModuleHandler.startModule(view.getContext(), it.packageName, it.activityName)
                    }
                }
            }
        }
    }


    fun startGame() {
        view?.let { view ->
            boardGame?.let {
                dynamicModuleHandler.startModule(view.getContext(), it.packageName, it.activityName)
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