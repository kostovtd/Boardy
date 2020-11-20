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

    val user: User? = null


    fun signInWithEmailAndPassword(email: String, password: String) {
        view?.let {
            it.showLoading()
            userRepository.signInWithEmailAndPassword(this, email, password)
        }
    }


    override fun handleSignInWithEmailAndPassword(resource: Resource<User>) {
        view?.let {
            it.hideLoading()

            when(resource.status) {
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
}