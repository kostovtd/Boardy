package com.kostovtd.boardy.presenters

import androidx.annotation.VisibleForTesting
import androidx.core.util.PatternsCompat
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.ui.activities.SignUpView
import com.kostovtd.boardy.util.ErrorType
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 26.11.20.
 */
class SignUpPresent : BasePresenter<SignUpView>() {

    private val repository = UserRepository()


    fun signUpWithEmailAndPassword(email: String?, password: String?, confirmPassword: String?) {
        view?.let {
            if (isEmailEmpty(email)) {
                it.showError(ErrorType.EMPTY_EMAIL)
                return
            }

            if (!isEmailFormatValid(email)) {
                it.showError(ErrorType.WRONG_EMAIL_FORMAT)
                return
            }

            if (isPasswordEmpty(password)) {
                it.showError(ErrorType.EMPTY_PASSWORD)
                return
            }

            if (!isPasswordFormatValid(password)) {
                it.showError(ErrorType.WRONG_PASSWORD_FORMAT)
                return
            }

            if (isConfirmPasswordEmpty(confirmPassword)) {
                it.showError(ErrorType.EMPTY_CONFIRM_PASSWORD)
                return
            }

            if (!isConfirmPasswordSameAsPassword(confirmPassword, password)) {
                it.showError(ErrorType.PASSWORDS_MISMATCH)
                return
            }

            scopeIO.launch {

                scopeMainThread.launch {
                    it.showLoading()
                    it.disableAllViews()
                }

                val signUpResponse =
                    repository.signUpWithEmailAndPassword(email ?: "", password ?: "")

                when (signUpResponse.status) {
                    ResourceStatus.SUCCESS -> {
                        val signInResponse =
                            repository.signInWithEmailAndPassword(email ?: "", password ?: "")

                        scopeMainThread.launch {
                            it.hideLoading()
                            it.enableAllViews()

                            if (signInResponse.status == ResourceStatus.SUCCESS) {
                                it.goToMainActivity()
                                it.finishActivity()
                            } else {
                                signInResponse.error?.let { errorType ->
                                    it.showError(errorType)
                                }
                            }
                        }
                    }
                    ResourceStatus.ERROR -> {
                        scopeMainThread.launch {
                            it.hideLoading()
                            it.enableAllViews()

                            signUpResponse.error?.let { errorType ->
                                it.showError(errorType)
                            }
                        }
                    }
                }
            }
        }
    }


    @VisibleForTesting
    fun isEmailEmpty(email: CharSequence?): Boolean = email.isNullOrBlank()

    @VisibleForTesting
    fun isEmailFormatValid(email: CharSequence?): Boolean =
        email?.let { PatternsCompat.EMAIL_ADDRESS.matcher(it).matches() } ?: run { false }

    @VisibleForTesting
    fun isPasswordEmpty(password: String?): Boolean = password.isNullOrBlank()

    @VisibleForTesting
    fun isPasswordFormatValid(password: String?): Boolean =
        password?.let { it.length >= 8 } ?: run { false }

    @VisibleForTesting
    fun isConfirmPasswordEmpty(confirmPassword: String?): Boolean =
        confirmPassword.isNullOrBlank()

    @VisibleForTesting
    fun isConfirmPasswordSameAsPassword(
        confirmPassword: String?,
        password: String?
    ): Boolean = confirmPassword == password
}