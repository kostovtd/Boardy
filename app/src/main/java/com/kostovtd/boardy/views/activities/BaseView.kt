package com.kostovtd.boardy.views.activities

import com.kostovtd.boardy.data.ErrorType

/**
 * Created by tosheto on 17.11.20.
 */
interface BaseView {

    fun showLoading() {}
    fun hideLoading() {}
    fun showError(errorType: ErrorType) {}
    fun finishActivity() {}

}