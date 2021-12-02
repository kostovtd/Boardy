package com.kostovtd.boardy.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kostovtd.boardy.R


/**
 * Created by tosheto on 20.11.20.
 */
class MessageHandler(
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
            ErrorType.QR_SCANNING_FAILED -> context.getString(R.string.qr_scanning_failed)
            else -> context.getString(R.string.something_went_wrong)
        }

        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
    }


    fun showInfoScankbar(infoType: InfoType) {
        val infoMessage = when(infoType) {
            else -> {
                ""
            }
        }

        Snackbar.make(view, infoMessage, Snackbar.LENGTH_LONG).show()
    }


    fun showSuccessScankbar(successType: SuccessType) {
        val successMessage = when(successType) {
            else -> {
                ""
            }
        }

        Snackbar.make(view, successMessage, Snackbar.LENGTH_LONG).show()
    }
}


enum class ErrorType {
    UNKNOWN,
    EMPTY_EMAIL,
    WRONG_EMAIL_FORMAT,
    EMPTY_PASSWORD,
    WRONG_PASSWORD_FORMAT,
    WRONG_CREDENTIALS,
    EMPTY_CONFIRM_PASSWORD,
    PASSWORDS_MISMATCH,
    FIREBASE_AUTH_WEAK_PASSWORD,
    FIREBASE_AUTH_INVALID_CREDENTIALS,
    FIREBASE_AUTH_USER_COLLISION,
    FIREBASE_AUTH_INVALID_USER,
    FIREBASE_CREATE_GAME_SESSION,
    FIREBASE_GET_GAME_SESSION_BY_ID,
    FIRESTORE_UPDATE_GAME_SESSION,
    FIRESTORE_FIND_GAME_SESSION,
    DATABASE_FIND_GAME_SESSION,
    DATABASE_FIND_BOARD_GAME,
    QR_SCANNING_FAILED,

}


enum class InfoType {

}


enum class SuccessType {

}