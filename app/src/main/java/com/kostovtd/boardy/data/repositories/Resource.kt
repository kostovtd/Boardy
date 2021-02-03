package com.kostovtd.boardy.data.repositories

import com.kostovtd.boardy.util.ErrorType

/**
 * Created by tosheto on 15.11.20.
 */
data class Resource<out T>(val status: ResourceStatus, val data: T?, val error: ErrorType? = null)


enum class ResourceStatus {
    SUCCESS,
    ERROR
}