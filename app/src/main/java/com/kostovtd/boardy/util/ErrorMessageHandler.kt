package com.kostovtd.boardy.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.ErrorType

/**
 * Created by tosheto on 20.11.20.
 */
class ErrorMessageHandler(private val context: Context,
                          private val view: View) {


    fun showErrorSnackbar(errorType: ErrorType) {
        val errorMessage = when(errorType) {
            ErrorType.UNKNOWN -> context.getString(R.string.something_went_wrong)
            ErrorType.WRONG_CREDENTIALS -> context.getString(R.string.wrong_credentials)
        }

        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
    }

}