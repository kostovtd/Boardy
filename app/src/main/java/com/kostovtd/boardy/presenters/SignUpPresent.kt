package com.kostovtd.boardy.presenters

import androidx.core.util.PatternsCompat
import com.kostovtd.boardy.data.ErrorType
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.views.activities.SignUpView

/**
 * Created by tosheto on 26.11.20.
 */
class SignUpPresent : BasePresenter<SignUpView>() {

    private val repository = UserRepository()


    fun signUpWithEmailAndPassword(
        email: String?,
        password: String?,
        confirmPassword: String?,
        userName: String?,
        firstName: String?,
        lastName: String?
    ) {
        view?.let {
            if(isEmailEmpty(email)) {
                it.showError(ErrorType.EMPTY_EMAIL)
                return
            }

            if(!isEmailFormatValid(email)) {
                it.showError(ErrorType.WRONG_EMAIL_FORMAT)
                return
            }

            if(isPasswordEmpty(password)) {
                it.showError(ErrorType.EMPTY_PASSWORD)
                return
            }

            if(!isPasswordFormatValid(password)) {
                it.showError(ErrorType.WRONG_PASSWORD_FORMAT)
                return
            }

            if(isConfirmPasswordEmpty(confirmPassword)) {
                it.showError(ErrorType.EMPTY_CONFIRM_PASSWORD)
                return
            }

            if(!isConfirmPasswordSameAsPassword(confirmPassword, password)) {
                it.showError(ErrorType.PASSWORDS_MISMATCH)
                return
            }

            it.showLoading()
        }
    }


    private fun isEmailEmpty(email: CharSequence?): Boolean = email.isNullOrBlank()

    private fun isEmailFormatValid(email: CharSequence?): Boolean = email?.let { PatternsCompat.EMAIL_ADDRESS.matcher(it).matches() } ?: run { false }

    private fun isPasswordEmpty(password: String?): Boolean = password.isNullOrBlank()

    private fun isPasswordFormatValid(password: String?): Boolean = password?.let { it.length >= 8 } ?: run { false }

    private fun isConfirmPasswordEmpty(confirmPassword: String?): Boolean = confirmPassword.isNullOrBlank()

    private fun isConfirmPasswordSameAsPassword(confirmPassword: String?, password: String?): Boolean = confirmPassword == password
}