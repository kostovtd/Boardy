package com.kostovtd.boardy.views.activities

import android.content.Context
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
    fun finishActivity() {}
    fun disableAllViews() {}
    fun enableAllViews() {}
    fun getContext(): Context

}