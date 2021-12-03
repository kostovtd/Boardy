package com.kostovtd.boardy.presenters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kostovtd.boardy.data.repositories.Resource
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.views.activities.BaseView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by tosheto on 16.11.20.
 */
abstract class BasePresenter<V> {

    protected var view: V? = null
    private var job = Job()
    protected val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    protected val scopeIO = CoroutineScope(job + Dispatchers.IO)
    protected val userRepository = UserRepository()


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
        Gson().fromJson(json, typeToken.type)

    protected fun <T> handleResponse(response: Resource<T>): Boolean =
        view?.let {
            when (response.status) {
                ResourceStatus.SUCCESS -> {
                    true
                }
                ResourceStatus.ERROR -> {
                    false
                }
            }
        } ?: run {
            false
        }

    protected fun handleError(error: ErrorType?) {
        view?.let { view ->
            error?.let { error ->
                scopeMainThread.launch {
                    (view as BaseView).hideLoading()
                    (view as BaseView).enableAllViews()
                    (view as BaseView).showError(error)
                }
            }  ?: run {
                (view as BaseView).showError(ErrorType.UNKNOWN)
            }
        }
    }

    fun getCurrentUserId(): String? = userRepository.getCurrentUser()?.uid

    fun getCurrentUserEmail(): String? = userRepository.getCurrentUser()?.email
}