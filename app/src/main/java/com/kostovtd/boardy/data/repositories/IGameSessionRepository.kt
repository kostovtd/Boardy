package com.kostovtd.boardy.data.repositories

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.util.ErrorType

/**
 * Created by tosheto on 22.02.21.
 */
interface IGameSessionRepository {
    fun onGameSessionDatabaseUpdated(gameSessionDatabase: GameSessionDatabase) {}
    fun onGameSessionDatabaseUpdateError(errorType: ErrorType) {}
}