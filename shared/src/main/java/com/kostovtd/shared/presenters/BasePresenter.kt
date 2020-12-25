package com.kostovtd.shared.presenters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Created by tosheto on 16.11.20.
 */
abstract class BasePresenter<V> {

    protected var view: V? = null
    private var job = Job()
    protected val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    protected val scopeIO = CoroutineScope(job + Dispatchers.IO)


    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    open fun cancelAllRequests() {
        job.cancel()
    }

    protected fun <T> parseJson(json: String, typeToken: TypeToken<T>): T =
        Gson().fromJson<T>(json, typeToken.type)
}