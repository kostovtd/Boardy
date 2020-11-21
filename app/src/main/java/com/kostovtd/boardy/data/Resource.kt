package com.kostovtd.boardy.data

/**
 * Created by tosheto on 15.11.20.
 */
data class Resource<out T>(val status: ResourceStatus, val data: T?, val error: ErrorType? = null) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(ResourceStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ResourceStatus.ERROR, data)
        }
    }
}


enum class ErrorType {
    UNKNOWN,
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    WRONG_CREDENTIALS
}


enum class ResourceStatus {
    SUCCESS,
    ERROR
}