package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.MainView
import com.kostovtd.shared.presenters.BasePresenter

/**
 * Created by tosheto on 21.11.20.
 */
class MainPresenter : BasePresenter<MainView>() {

    private val userRepository = UserRepository()

    fun signOut() {
        view?.let {
            userRepository.signOut()
            it.goToSignInActivity()
            it.finishActivity()
        }
    }
}