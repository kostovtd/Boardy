package com.kostovtd.boardy.data.repositories

import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.web.APIClient

/**
 * Created by tosheto on 23.01.21.
 */
class BoardGamesRepository {

    suspend fun findGamesByName(name: String): Resource<ArrayList<BoardGame>> {
        val response = APIClient.firebaseAPI.getBoardGameByName(name)

        return if(response.success) {
            Resource(ResourceStatus.SUCCESS, response.data)
        } else {
            Resource(ResourceStatus.ERROR, null, ErrorType.DATABASE_FIND_GAME_SESSION)
        }
    }
}