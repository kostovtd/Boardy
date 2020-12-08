package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.ErrorType
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.SignInView
import kotlinx.coroutines.async

/**
 * Created by tosheto on 17.11.20.
 */
class SignInPresenter : BasePresenter<SignInView>() {

    private val userRepository = UserRepository()


    fun signInWithEmailAndPassword(email: String, password: String) {
        view?.let {
            if (isEmailInputValid(email)) {
                if (isPasswordInputValid(password)) {
                    it.showLoading()

                    scopeMainThread.async {
                        val signInResponse =
                            userRepository.signInWithEmailAndPassword(email, password)
                        it.hideLoading()

                        when (signInResponse.status) {
                            ResourceStatus.SUCCESS -> {
                                it.goToMainActivity()
                                it.finishActivity()
                            }
                            ResourceStatus.ERROR -> {
                                signInResponse.error?.let { errorType ->
                                    it.showError(errorType)
                                }
                            }
                        }
                    }
                } else {
                    it.showError(ErrorType.EMPTY_PASSWORD)
                }
            } else {
                it.showError(ErrorType.EMPTY_EMAIL)
            }
        }
    }


    private fun isEmailInputValid(email: String?): Boolean = !email.isNullOrBlank()


    private fun isPasswordInputValid(password: String?): Boolean = !password.isNullOrBlank()
}