package com.kostovtd.boardy.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.repositories.ErrorType

/**
 * Created by tosheto on 20.11.20.
 */
class ErrorMessageHandler(
    private val context: Context,
    private val view: View
) {


    fun showErrorSnackbar(errorType: ErrorType) {
        val errorMessage = when (errorType) {
            ErrorType.UNKNOWN -> context.getString(R.string.something_went_wrong)
            ErrorType.WRONG_CREDENTIALS -> context.getString(R.string.wrong_credentials)
            ErrorType.EMPTY_EMAIL -> context.getString(R.string.empty_email)
            ErrorType.EMPTY_PASSWORD -> context.getString(R.string.empty_password)
            ErrorType.WRONG_EMAIL_FORMAT -> context.getString(R.string.wrong_email_format)
            ErrorType.WRONG_PASSWORD_FORMAT -> context.getString(R.string.wrong_password_format)
            ErrorType.EMPTY_CONFIRM_PASSWORD -> context.getString(R.string.empty_confirm_password)
            ErrorType.PASSWORDS_MISMATCH -> context.getString(R.string.password_mismatch)
            ErrorType.FIREBASE_AUTH_WEAK_PASSWORD -> context.getString(R.string.weak_password)
            ErrorType.FIREBASE_AUTH_USER_COLLISION -> context.getString(R.string.email_is_already_in_use)
            else -> context.getString(R.string.something_went_wrong)
        }

        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
    }

}