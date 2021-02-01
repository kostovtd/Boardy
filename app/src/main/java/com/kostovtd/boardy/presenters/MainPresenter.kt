package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.repositories.BoardGamesRepository
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.MainView
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 21.11.20.
 */
class MainPresenter : BasePresenter<MainView>() {

    private val userRepository = UserRepository()
    private val boardGamesRepository = BoardGamesRepository()


    fun signOut() {
        view?.let {
            userRepository.signOut()
            it.goToSignInActivity()
            it.finishActivity()
        }
    }


    fun findBoardGames() {
        scopeIO.launch {
            val findBoardGamesResponse = boardGamesRepository.findGamesByName("Hero")
            when(findBoardGamesResponse.status) {

            }
        }
    }
}