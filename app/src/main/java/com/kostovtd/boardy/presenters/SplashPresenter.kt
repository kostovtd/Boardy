package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.ui.activities.SplashView

/**
 * Created by tosheto on 22.11.20.
 */
class SplashPresenter: BasePresenter<SplashView>() {

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