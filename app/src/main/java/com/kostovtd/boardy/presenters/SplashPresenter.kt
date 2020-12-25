package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.SplashView
import com.kostovtd.shared.presenters.BasePresenter

/**
 * Created by tosheto on 22.11.20.
 */
class SplashPresenter: BasePresenter<SplashView>() {

    private val userRepository = UserRepository()


    fun isSignedIn() {
        view?.let {
            if(userRepository.isSignedIn()) {
                it.goToMainActivity()
            } else {
                it.goToSignInActivity()
            }
            it.finishActivity()
        }
    }


}