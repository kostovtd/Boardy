package com.kostovtd.boardy.util

/**
 * Created by tosheto on 2.02.21.
 */
object Constants {
    // --------- FIRESTORE ---------
    const val BOARDGAMES_COLLECTION_PATH = "boardGames"
    const val GAME_SESSIONS_COLLECTION_PATH = "gameSessions"
    const val NAME_FIELD = "name"
    const val PLAYERS_FIELD = "players"
    const val TEAMS_FIELD = "teams"
    const val START_TIME_FIELD = "startTime"
    const val FIRESTORE_VALUE_SEPARATOR = "|"

    // --------- REALTIME DATABASE ---------
    const val POINTS_CHILD = "points"
    const val GAME_SESSION_CHILD = "gameSession"

    // --------- INTENT EXTRA KEYS ---------
    const val GAME_SESSION_ID_KEY = "gameSessionIdKey"
    const val BOARD_GAME_ID_KEY = "boardGameId"
    const val BOARD_GAME_KEY = "boardGame"
    const val GAME_SESSION_FIRESTORE_KEY = "gameSessionFirestore"
    const val PLAYER_TYPE_KEY = "playerType"
    const val BOARD_GAME_GAME_SESSION_KEY = "boardGameGameSession"
}