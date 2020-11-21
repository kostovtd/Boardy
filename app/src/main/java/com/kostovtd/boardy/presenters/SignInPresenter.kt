package com.kostovtd.boardy.presenters

import com.kostovtd.boardy.data.ErrorType
import com.kostovtd.boardy.data.Resource
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.data.models.User
import com.kostovtd.boardy.data.repositories.IUserRepositoryListener
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.SignInView

/**
 * Created by tosheto on 17.11.20.
 */
class SignInPresenter : BasePresenter<SignInView>(), IUserRepositoryListener {

    private val userRepository = UserRepository()


    fun signInWithEmailAndPassword(email: String, password: String) {
        view?.let {
            if (isEmailInputValid(email)) {
                if (isPasswordInputValid(password)) {
                    it.showLoading()
                    userRepository.signInWithEmailAndPassword(this, email, password)
                } else {
                    it.showError(ErrorType.EMPTY_PASSWORD)
                }
            } else {
                it.showError(ErrorType.EMPTY_EMAIL)
            }
        }
    }


    override fun handleSignInWithEmailAndPassword(resource: Resource<User>) {
        view?.let {
            it.hideLoading()

            when (resource.status) {
                ResourceStatus.SUCCESS -> {
                    it.goToMainActivity()
                    it.finishActivity()
                }
                ResourceStatus.ERROR -> {
                    resource.error?.let { errorType ->
                        it.showError(errorType)
                    }
                }
            }
        }
    }


    private fun isEmailInputValid(email: String?): Boolean = !email.isNullOrBlank()


    private fun isPasswordInputValid(password: String?): Boolean = !password.isNullOrBlank()
}