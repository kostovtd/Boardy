package com.kostovtd.boardy.data.repositories

import androidx.annotation.StringRes
import com.kostovtd.boardy.R
import com.kostovtd.boardy.util.ErrorType

/**
 * Created by tosheto on 15.11.20.
 */
@Deprecated("Replace with ResourceWithMessage")
data class Resource<out T>(val status: ResourceStatus, val data: T?, val error: ErrorType? = null)

data class ResourceWithMessage<out T>(val status: ResourceStatus, val data: T?, val message: MessageType? = null)


enum class ResourceStatus {
    SUCCESS,
    ERROR
}

enum class MessageType(@StringRes val resourceId: Int) {
    NONE(R.string.not_available),
    FIRESTORE_UPDATE_GAME_SESSION_ERROR(R.string.data_could_not_be_updated_error),
    FIREBASE_GET_GAME_SESSION_BY_ID_ERROR(R.string.data_could_not_be_loaded_error),
    CREATE_GAME_SESSION_ERROR(R.string.can_not_create_game_session_error),
    CREATE_QR_CODE_ERROR(R.string.qr_code_can_not_be_created_error),
    ADD_CURRENT_PLAYER_TO_GAME_SESSION_ERROR(R.string.add_current_player_to_game_session_error)
}