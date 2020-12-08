package com.kostovtd.boardy.data

/**
 * Created by tosheto on 15.11.20.
 */
data class Resource<out T>(val status: ResourceStatus, val data: T?, val error: ErrorType? = null)


enum class ErrorType {
    UNKNOWN,
    EMPTY_EMAIL,
    WRONG_EMAIL_FORMAT,
    EMPTY_PASSWORD,
    WRONG_PASSWORD_FORMAT,
    WRONG_CREDENTIALS,
    EMPTY_CONFIRM_PASSWORD,
    PASSWORDS_MISMATCH,
    FIREBASE_AUTH_WEAK_PASSWORD,
    FIREBASE_AUTH_INVALID_CREDENTIALS,
    FIREBASE_AUTH_USER_COLLISION,
    FIREBASE_AUTH_INVALID_USER
}


enum class ResourceStatus {
    SUCCESS,
    ERROR
}