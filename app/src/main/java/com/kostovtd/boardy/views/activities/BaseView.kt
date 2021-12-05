package com.kostovtd.boardy.views.activities

import android.content.Context
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.InfoType
import com.kostovtd.boardy.util.SuccessType


/**
 * Created by tosheto on 17.11.20.
 */
interface BaseView {

    fun showLoading() {}
    fun hideLoading() {}
    fun showError(errorType: ErrorType) {}
    fun showSuccess(successType: SuccessType) {}
    fun showInfo(infoType: InfoType) {}
    fun showConfirmationDialog(title: String, description: String, positiveCallback: () -> Unit) {}
    fun showConfirmationDialog(titleResourceId: Int, descriptionResourceId: Int, positiveCallback: () -> Unit) {}
    fun showInformationDialog(title: String, description: String, callback: () -> Unit) {}
    fun showInformationDialog(titleResourceId: Int, descriptionResourceId: String, callback: () -> Unit) {}
    fun finishActivity() {}
    fun disableAllViews() {}
    fun enableAllViews() {}
    fun handleGameSessionChanges(gameSessionDatabase: GameSessionDatabase) {}
    fun getViewContext(): Context?

}