package com.kostovtd.herorealms.views.activities

import com.kostovtd.shared.ErrorType

/**
 * Created by tosheto on 25.12.20.
 */
interface HeroRealmsView {
    fun showLoading() {}
    fun hideLoading() {}
    fun showError(errorType: ErrorType) {}
    fun finishActivity() {}
    fun disableAllViews() {}
    fun enableAllViews() {}
}