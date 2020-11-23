package com.kostovtd.boardy.presenters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by tosheto on 16.11.20.
 */
abstract class BasePresenter<V> {

    protected var view: V? = null

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    open fun cancelAllRequests() {}

    protected fun <T> parseJson(json: String, typeToken: TypeToken<T>): T = Gson().fromJson<T>(json, typeToken.type)
}